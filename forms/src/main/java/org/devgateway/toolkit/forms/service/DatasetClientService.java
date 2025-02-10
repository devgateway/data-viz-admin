package org.devgateway.toolkit.forms.service;

import org.apache.commons.io.FileUtils;
import org.devgateway.toolkit.forms.client.DataSetClientException;
import org.devgateway.toolkit.forms.client.DatasetClient;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.dao.data.Dataset;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dto.ServiceMetadata;
import org.devgateway.toolkit.persistence.service.data.CSVDatasetService;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.devgateway.toolkit.forms.client.ClientConstants.CODE_PREFIX;
import static org.devgateway.toolkit.forms.client.ClientConstants.JobStatus.COMPLETED;
import static org.devgateway.toolkit.forms.client.ClientConstants.JobStatus.ERROR;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DRAFT;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.ERROR_IN_PUBLISHING;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.ERROR_IN_UNPUBLISHING;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.PUBLISHED;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.PUBLISHING;

@Service
public class DatasetClientService {

    private static final Logger logger = LoggerFactory.getLogger(DatasetClientService.class);

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
            ServiceMetadata serviceMetadata = eurekaClientService.findByName(d.getDestinationService());
            DatasetClient client = new DatasetClient(serviceMetadata.getUrl());
            String status = client.getDatasetJobStatus(CODE_PREFIX + d.getId()).getStatus();
            String initialStatus = d.getStatus();
            if (COMPLETED.equals(status)) {
                String completedStatus = getCompletedStatus(initialStatus);
                d.setStatus(completedStatus);
                logger.info(String.format("The dataset with id %s changed the status from %s to %s",
                        d.getId(), initialStatus, completedStatus));
            } else if (ERROR.equals(status)) {
                String errorStatus = getErrorStatus(initialStatus);
                d.setStatus(errorStatus);
                logger.info(String.format("The dataset with id %s changed the status from %s to %s",
                        d.getId(), initialStatus, errorStatus));
            }

            if (!initialStatus.equals(d.getStatus())) {
                if (d instanceof TetsimDataset) {
                    tetsimDatasetService.save((TetsimDataset) d);
                } else if (d instanceof CSVDataset) {
                    csvDatasetService.save((CSVDataset) d);
                } else {
                    throw new RuntimeException("Invalid dataset class");
                }
            }
        });
    }

    private String getCompletedStatus(final String status) {
        return PUBLISHING.equals(status) ? PUBLISHED : DRAFT;
    }

    private String getErrorStatus(final String status) {
        return PUBLISHING.equals(status) ? ERROR_IN_PUBLISHING : ERROR_IN_UNPUBLISHING;
    }

    public void publishDataset(Dataset dataset, String fileName, byte[] content) throws DataSetClientException {
        String serviceURL = getDestinationService(dataset).getUrl();
        DatasetClient client = new DatasetClient(serviceURL);

        String description = dataset.getDescription();
        String name = "Dataset " + dataset.getYear();
        if (description != null){
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

    public void unpublishDataset(Dataset dataset) throws DataSetClientException {
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
