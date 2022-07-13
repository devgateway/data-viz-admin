package org.devgateway.toolkit.forms.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.devgateway.toolkit.forms.client.DataSetClientException;
import org.devgateway.toolkit.forms.client.DatasetClient;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dto.ServiceMetadata;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.devgateway.toolkit.persistence.service.tetsim.TetsimOutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.devgateway.toolkit.forms.client.ClientConstants.EXTERNAL_ID_PREFIX;
import static org.devgateway.toolkit.forms.client.ClientConstants.JobStatus.COMPLETED;
import static org.devgateway.toolkit.forms.client.ClientConstants.JobStatus.ERROR;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.NOT_PUBLISHED;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.PUBLISHED;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.PUBLISHING;

@Service
public class DatasetPublishingService {

    private static final Logger logger = LoggerFactory.getLogger(DatasetPublishingService.class);

    @Autowired
    private TetsimOutputService tetsimOutputService;

    @Autowired
    private TetsimDatasetService tetsimDatasetService;

    @Autowired
    private EurekaClientService eurekaClientService;

    @Scheduled(cron = "0 * * * * *")
    public void triggerCheckDatasetsJob() {
        logger.info("Fired triggerCheckDatasetsJob");

        tetsimDatasetService.findAllPublishing().forEach(d -> {
            ServiceMetadata serviceMetadata = eurekaClientService.getServiceByName(d.getDestinationService());
            DatasetClient client = new DatasetClient(serviceMetadata.getUrl());
            String status = client.getDatasetJobStatus(EXTERNAL_ID_PREFIX + d.getId()).getStatus();
            if (COMPLETED.equals(status)) {
                d.setStatus(PUBLISHED);
                logger.info(String.format("The dataset with id %s changed the status from %s to %s",
                        d.getId(), PUBLISHING, PUBLISHED));
            } else if (ERROR.equals(status)) {
                d.setStatus(NOT_PUBLISHED);
                logger.info(String.format("The dataset with id %s changed the status from %s to %s",
                        d.getId(), PUBLISHING, NOT_PUBLISHED));
            }

            if (!PUBLISHING.equals(d.getStatus())) {
                tetsimDatasetService.save(d);
            }
        });

    }

    public void publish(ServiceMetadata service, TetsimDataset dataset) throws DataSetClientException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        byte[] tetsimCSVDatasetOutputs = tetsimOutputService.getTetsimCSVDatasetOutputs(dataset.getId());

        DatasetClient client = new DatasetClient(service.getUrl());
        client.publishDataset(dataset, tetsimCSVDatasetOutputs);
    }
}
