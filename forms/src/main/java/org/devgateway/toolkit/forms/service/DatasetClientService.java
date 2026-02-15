package org.devgateway.toolkit.forms.service;

import static org.devgateway.toolkit.forms.client.ClientConstants.CODE_PREFIX;
import static org.devgateway.toolkit.forms.client.ClientConstants.JobStatus.COMPLETED;
import static org.devgateway.toolkit.forms.client.ClientConstants.JobStatus.ERROR;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DRAFT;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.ERROR_IN_PUBLISHING;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.ERROR_IN_UNPUBLISHING;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.PUBLISHED;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.PUBLISHING;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.devgateway.toolkit.forms.client.DataSetClientException;
import org.devgateway.toolkit.forms.client.DatasetClient;
import org.devgateway.toolkit.forms.client.DatasetJobStatus;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.dao.data.Dataset;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dto.ServiceMetadata;
import org.devgateway.toolkit.persistence.service.data.CSVDatasetService;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DatasetClientService {

    private static final Logger logger = LoggerFactory.getLogger(
        DatasetClientService.class
    );

    /**
     * Timeout in minutes for publishing jobs. If a job has been in PUBLISHING state
     * for longer than this duration, it will be marked as ERROR_IN_PUBLISHING.
     * Default is 30 minutes.
     */
    @Value("${dataset.publishing.timeout.minutes:30}")
    private int publishingTimeoutMinutes;

    @Autowired
    private TetsimDatasetService tetsimDatasetService;

    @Autowired
    private CSVDatasetService csvDatasetService;

    @Autowired
    private EurekaClientService eurekaClientService;

    @Scheduled(cron = "0 * * * * *")
    public void triggerCheckDatasetsJob() {
        logger.debug("Fired triggerCheckDatasetsJob");
        List<Dataset> datasets = new ArrayList<>();
        datasets.addAll(tetsimDatasetService.findAllInProgress());
        datasets.addAll(csvDatasetService.findAllInProgress());

        checkDatasetJobs(datasets);
    }

    private void checkDatasetJobs(List<Dataset> datasets) {
        datasets.forEach(d -> {
            try {
                ServiceMetadata serviceMetadata =
                    eurekaClientService.findByName(d.getDestinationService());
                DatasetClient client = new DatasetClient(
                    serviceMetadata.getUrl()
                );
                DatasetJobStatus jobStatus = client.getDatasetJobStatus(
                    CODE_PREFIX + d.getId()
                );
                String initialStatus = d.getStatus();

                if (jobStatus == null) {
                    // Job not found on remote service - check if it's been stuck for too long
                    if (isJobStuck(d)) {
                        markAsErrorDueToTimeout(d, initialStatus);
                    }
                    return;
                }

                String remoteStatus = jobStatus.getStatus();

                if (COMPLETED.equals(remoteStatus)) {
                    String completedStatus = getCompletedStatus(initialStatus);
                    d.setStatus(completedStatus);
                    logger.info(
                        String.format(
                            "The dataset with id %s changed the status from %s to %s",
                            d.getId(),
                            initialStatus,
                            completedStatus
                        )
                    );
                } else if (ERROR.equals(remoteStatus)) {
                    String errorStatus = getErrorStatus(initialStatus);
                    d.setStatus(errorStatus);
                    logger.info(
                        String.format(
                            "The dataset with id %s changed the status from %s to %s. Error message: %s",
                            d.getId(),
                            initialStatus,
                            errorStatus,
                            jobStatus.getMessage()
                        )
                    );
                } else {
                    // Job is still processing - check for timeout
                    if (isJobStuck(d, jobStatus)) {
                        markAsErrorDueToTimeout(d, initialStatus);
                    }
                }

                if (!initialStatus.equals(d.getStatus())) {
                    saveDataset(d);
                }
            } catch (Exception e) {
                logger.error(
                    String.format(
                        "Error checking job status for dataset %s: %s",
                        d.getId(),
                        e.getMessage()
                    ),
                    e
                );
                // Check if the job has been stuck for too long even if we can't reach the service
                if (isJobStuck(d)) {
                    String initialStatus = d.getStatus();
                    markAsErrorDueToTimeout(d, initialStatus);
                    saveDataset(d);
                }
            }
        });
    }

    /**
     * Check if a job has been stuck in PUBLISHING/UNPUBLISHING state for too long
     * based on the dataset's last modified date.
     */
    private boolean isJobStuck(Dataset d) {
        if (d.getLastModifiedDate() == null) {
            return false;
        }
        ZonedDateTime lastModified = d.getLastModifiedDate().orElse(null);
        if (lastModified == null) {
            return false;
        }
        Duration duration = Duration.between(lastModified, ZonedDateTime.now());
        return duration.toMinutes() > publishingTimeoutMinutes;
    }

    /**
     * Check if a job has been stuck based on the remote job's created date.
     */
    private boolean isJobStuck(Dataset d, DatasetJobStatus jobStatus) {
        if (jobStatus.getCreatedDate() == null) {
            return isJobStuck(d);
        }
        Duration duration = Duration.between(
            jobStatus.getCreatedDate(),
            ZonedDateTime.now()
        );
        return duration.toMinutes() > publishingTimeoutMinutes;
    }

    private void markAsErrorDueToTimeout(Dataset d, String initialStatus) {
        String errorStatus = getErrorStatus(initialStatus);
        d.setStatus(errorStatus);
        logger.warn(
            String.format(
                "Dataset with id %s has been in %s state for more than %d minutes. " +
                    "Marking as %s due to timeout. This may indicate a problem with the remote service.",
                d.getId(),
                initialStatus,
                publishingTimeoutMinutes,
                errorStatus
            )
        );
    }

    private void saveDataset(Dataset d) {
        if (d instanceof TetsimDataset) {
            tetsimDatasetService.save((TetsimDataset) d);
        } else if (d instanceof CSVDataset) {
            csvDatasetService.save((CSVDataset) d);
        } else {
            throw new RuntimeException("Invalid dataset class");
        }
    }

    private String getCompletedStatus(final String status) {
        return PUBLISHING.equals(status) ? PUBLISHED : DRAFT;
    }

    private String getErrorStatus(final String status) {
        return PUBLISHING.equals(status)
            ? ERROR_IN_PUBLISHING
            : ERROR_IN_UNPUBLISHING;
    }

    /**
     * Cancel a stuck publishing job by resetting the dataset status to ERROR_IN_PUBLISHING.
     * This allows the user to retry the upload after fixing any issues.
     *
     * @param dataset the dataset to cancel publishing for
     */
    public void cancelPublishing(Dataset dataset) {
        String initialStatus = dataset.getStatus();
        if (!PUBLISHING.equals(initialStatus)) {
            throw new IllegalStateException(
                String.format(
                    "Cannot cancel publishing for dataset %s: current status is %s, expected PUBLISHING",
                    dataset.getId(),
                    initialStatus
                )
            );
        }

        // Try to delete the dataset from the remote service to clean up partial data
        try {
            String code = CODE_PREFIX + dataset.getId();
            String serviceURL = getDestinationService(dataset).getUrl();
            new DatasetClient(serviceURL).unpublishDataset(code);
            logger.info(
                "Successfully requested cleanup of partial data for dataset {} on remote service",
                dataset.getId()
            );
        } catch (DataSetClientException e) {
            logger.warn(
                "Could not clean up partial data on remote service for dataset {}: {}. " +
                    "Manual database cleanup may be required.",
                dataset.getId(),
                e.getMessage()
            );
        } catch (Exception e) {
            logger.warn(
                "Could not clean up partial data on remote service for dataset {}: {}. " +
                    "Manual database cleanup may be required.",
                dataset.getId(),
                e.getMessage()
            );
        }

        dataset.setStatus(ERROR_IN_PUBLISHING);
        saveDataset(dataset);
        logger.info(
            "Cancelled publishing for dataset {}. Status changed from {} to {}",
            dataset.getId(),
            initialStatus,
            ERROR_IN_PUBLISHING
        );
    }

    public void publishDataset(Dataset dataset, String fileName, byte[] content)
        throws DataSetClientException {
        String serviceURL = getDestinationService(dataset).getUrl();
        DatasetClient client = new DatasetClient(serviceURL);

        String description = dataset.getDescription();
        String name = "Dataset " + dataset.getYear();
        if (description != null) {
            name = description;
        }
        String code = CODE_PREFIX + dataset.getId();

        File tempUploadFile;
        try {
            tempUploadFile = File.createTempFile(fileName, null);
            tempUploadFile.deleteOnExit();
            FileUtils.writeByteArrayToFile(tempUploadFile, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        client.publishDataset(name, code, tempUploadFile);
    }

    public void unpublishDataset(Dataset dataset)
        throws DataSetClientException {
        String code = CODE_PREFIX + dataset.getId();
        String serviceURL = getDestinationService(dataset).getUrl();

        new DatasetClient(serviceURL).unpublishDataset(code);
    }

    private ServiceMetadata getDestinationService(Dataset dataset) {
        String destinationService = dataset.getDestinationService();
        return eurekaClientService.findByName(destinationService);
    }

    public byte[] getTemplateDownload(final String serviceName) {
        ServiceMetadata service = eurekaClientService.findByName(serviceName);
        return new DatasetClient(service.getUrl()).getTemplateDownload();
    }
}
