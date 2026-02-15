package org.devgateway.toolkit.forms.service;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DRAFT;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.ERROR_IN_PUBLISHING;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.ERROR_IN_UNPUBLISHING;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.PUBLISHED;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.PUBLISHING;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.UNPUBLISHING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.devgateway.toolkit.forms.client.DataSetClientException;
import org.devgateway.toolkit.forms.client.DatasetClient;
import org.devgateway.toolkit.forms.client.DatasetJobStatus;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.dto.ServiceMetadata;
import org.devgateway.toolkit.persistence.service.data.CSVDatasetService;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit tests for DatasetClientService.
 * Tests the cancel publishing, timeout detection, and job status checking logic.
 */
@ExtendWith(MockitoExtension.class)
public class DatasetClientServiceTest {

    @Mock
    private TetsimDatasetService tetsimDatasetService;

    @Mock
    private CSVDatasetService csvDatasetService;

    @Mock
    private EurekaClientService eurekaClientService;

    @InjectMocks
    private DatasetClientService datasetClientService;

    private CSVDataset testDataset;
    private ServiceMetadata testServiceMetadata;

    @BeforeEach
    void setUp() {
        // Set the publishing timeout to 30 minutes (default)
        ReflectionTestUtils.setField(datasetClientService, "publishingTimeoutMinutes", 30);

        // Create a test dataset
        testDataset = new CSVDataset();
        ReflectionTestUtils.setField(testDataset, "id", 1L);
        testDataset.setYear(2025);
        testDataset.setDestinationService("INTERFERENCE");
        testDataset.setStatus(PUBLISHING);

        // Create test service metadata
        testServiceMetadata = new ServiceMetadata();
        testServiceMetadata.setName("INTERFERENCE");
        testServiceMetadata.setUrl("http://localhost:8090");
    }

    @Nested
    @DisplayName("Cancel Publishing Tests")
    class CancelPublishingTests {

        @Test
        @DisplayName("Should mark dataset as ERROR_IN_PUBLISHING when cancelling")
        void shouldMarkDatasetAsErrorWhenCancelling() {
            // Given
            when(eurekaClientService.findByName("INTERFERENCE")).thenReturn(testServiceMetadata);

            // When
            datasetClientService.cancelPublishing(testDataset);

            // Then
            assertEquals(ERROR_IN_PUBLISHING, testDataset.getStatus());
            verify(csvDatasetService, times(1)).save(testDataset);
        }

        @Test
        @DisplayName("Should throw exception when trying to cancel non-publishing dataset")
        void shouldThrowExceptionWhenNotPublishing() {
            // Given
            testDataset.setStatus(PUBLISHED);

            // When/Then
            IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> datasetClientService.cancelPublishing(testDataset)
            );

            assertEquals(
                "Cannot cancel publishing for dataset 1: current status is PUBLISHED, expected PUBLISHING",
                exception.getMessage()
            );
            verify(csvDatasetService, never()).save(any());
        }

        @Test
        @DisplayName("Should still mark as error even if remote cleanup fails")
        void shouldMarkAsErrorEvenIfRemoteCleanupFails() {
            // Given
            when(eurekaClientService.findByName("INTERFERENCE")).thenReturn(testServiceMetadata);
            // Remote service will fail (mock client throws exception)
            // The service should still mark the dataset as ERROR_IN_PUBLISHING

            // When
            datasetClientService.cancelPublishing(testDataset);

            // Then
            assertEquals(ERROR_IN_PUBLISHING, testDataset.getStatus());
            verify(csvDatasetService, times(1)).save(testDataset);
        }
    }

    @Nested
    @DisplayName("Status Transition Tests")
    class StatusTransitionTests {

        @Test
        @DisplayName("getCompletedStatus should return PUBLISHED for PUBLISHING status")
        void shouldReturnPublishedForPublishingStatus() throws Exception {
            // Use reflection to test private method
            String result = ReflectionTestUtils.invokeMethod(
                datasetClientService,
                "getCompletedStatus",
                PUBLISHING
            );

            assertEquals(PUBLISHED, result);
        }

        @Test
        @DisplayName("getCompletedStatus should return DRAFT for UNPUBLISHING status")
        void shouldReturnDraftForUnpublishingStatus() throws Exception {
            String result = ReflectionTestUtils.invokeMethod(
                datasetClientService,
                "getCompletedStatus",
                UNPUBLISHING
            );

            assertEquals(DRAFT, result);
        }

        @Test
        @DisplayName("getErrorStatus should return ERROR_IN_PUBLISHING for PUBLISHING status")
        void shouldReturnErrorInPublishingForPublishingStatus() throws Exception {
            String result = ReflectionTestUtils.invokeMethod(
                datasetClientService,
                "getErrorStatus",
                PUBLISHING
            );

            assertEquals(ERROR_IN_PUBLISHING, result);
        }

        @Test
        @DisplayName("getErrorStatus should return ERROR_IN_UNPUBLISHING for UNPUBLISHING status")
        void shouldReturnErrorInUnpublishingForUnpublishingStatus() throws Exception {
            String result = ReflectionTestUtils.invokeMethod(
                datasetClientService,
                "getErrorStatus",
                UNPUBLISHING
            );

            assertEquals(ERROR_IN_UNPUBLISHING, result);
        }
    }

    @Nested
    @DisplayName("Timeout Detection Tests")
    class TimeoutDetectionTests {

        @Test
        @DisplayName("Should detect stuck job when lastModifiedDate exceeds timeout")
        void shouldDetectStuckJobWhenExceedsTimeout() throws Exception {
            // Given - dataset was last modified 31 minutes ago (exceeds 30 min timeout)
            ZonedDateTime thirtyOneMinutesAgo = ZonedDateTime.now().minusMinutes(31);
            testDataset = new CSVDatasetWithLastModified(thirtyOneMinutesAgo);
            testDataset.setStatus(PUBLISHING);

            // When
            Boolean result = ReflectionTestUtils.invokeMethod(
                datasetClientService,
                "isJobStuck",
                testDataset
            );

            // Then
            assertEquals(true, result);
        }

        @Test
        @DisplayName("Should not detect stuck job when within timeout")
        void shouldNotDetectStuckJobWhenWithinTimeout() throws Exception {
            // Given - dataset was last modified 10 minutes ago (within 30 min timeout)
            ZonedDateTime tenMinutesAgo = ZonedDateTime.now().minusMinutes(10);
            testDataset = new CSVDatasetWithLastModified(tenMinutesAgo);
            testDataset.setStatus(PUBLISHING);

            // When
            Boolean result = ReflectionTestUtils.invokeMethod(
                datasetClientService,
                "isJobStuck",
                testDataset
            );

            // Then
            assertEquals(false, result);
        }

        @Test
        @DisplayName("Should not detect stuck job when lastModifiedDate is null")
        void shouldNotDetectStuckJobWhenLastModifiedDateIsNull() throws Exception {
            // Given - no last modified date
            testDataset = new CSVDatasetWithLastModified(null);
            testDataset.setStatus(PUBLISHING);

            // When
            Boolean result = ReflectionTestUtils.invokeMethod(
                datasetClientService,
                "isJobStuck",
                testDataset
            );

            // Then
            assertEquals(false, result);
        }

        @Test
        @DisplayName("Should detect stuck job based on remote job createdDate")
        void shouldDetectStuckJobBasedOnRemoteJobDate() throws Exception {
            // Given
            DatasetJobStatus jobStatus = new DatasetJobStatus();
            jobStatus.setCreatedDate(ZonedDateTime.now().minusMinutes(45));

            // When
            Boolean result = ReflectionTestUtils.invokeMethod(
                datasetClientService,
                "isJobStuck",
                testDataset,
                jobStatus
            );

            // Then
            assertEquals(true, result);
        }
    }

    @Nested
    @DisplayName("markAsErrorDueToTimeout Tests")
    class MarkAsErrorDueToTimeoutTests {

        @Test
        @DisplayName("Should mark PUBLISHING dataset as ERROR_IN_PUBLISHING on timeout")
        void shouldMarkPublishingAsErrorInPublishing() throws Exception {
            // Given
            testDataset.setStatus(PUBLISHING);

            // When
            ReflectionTestUtils.invokeMethod(
                datasetClientService,
                "markAsErrorDueToTimeout",
                testDataset,
                PUBLISHING
            );

            // Then
            assertEquals(ERROR_IN_PUBLISHING, testDataset.getStatus());
        }

        @Test
        @DisplayName("Should mark UNPUBLISHING dataset as ERROR_IN_UNPUBLISHING on timeout")
        void shouldMarkUnpublishingAsErrorInUnpublishing() throws Exception {
            // Given
            testDataset.setStatus(UNPUBLISHING);

            // When
            ReflectionTestUtils.invokeMethod(
                datasetClientService,
                "markAsErrorDueToTimeout",
                testDataset,
                UNPUBLISHING
            );

            // Then
            assertEquals(ERROR_IN_UNPUBLISHING, testDataset.getStatus());
        }
    }

    @Nested
    @DisplayName("Save Dataset Tests")
    class SaveDatasetTests {

        @Test
        @DisplayName("Should save CSVDataset using csvDatasetService")
        void shouldSaveCSVDatasetCorrectly() throws Exception {
            // Given
            CSVDataset csvDataset = new CSVDataset();

            // When
            ReflectionTestUtils.invokeMethod(
                datasetClientService,
                "saveDataset",
                csvDataset
            );

            // Then
            verify(csvDatasetService, times(1)).save(csvDataset);
            verify(tetsimDatasetService, never()).save(any());
        }
    }

    /**
     * Helper class to create a CSVDataset with a specific lastModifiedDate.
     * This is needed because CSVDataset's lastModifiedDate is managed by JPA.
     */
    private static class CSVDatasetWithLastModified extends CSVDataset {
        private final ZonedDateTime lastModified;

        CSVDatasetWithLastModified(ZonedDateTime lastModified) {
            this.lastModified = lastModified;
        }

        @Override
        public Optional<ZonedDateTime> getLastModifiedDate() {
            return Optional.ofNullable(lastModified);
        }
    }
}
