package org.devgateway.toolkit.forms.service;

import org.apache.commons.io.FileUtils;
import org.devgateway.toolkit.forms.client.DataSetClientException;
import org.devgateway.toolkit.forms.client.DatasetClient;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.dao.data.Dataset;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dto.ServiceMeasure;
import org.devgateway.toolkit.persistence.dto.ServiceMetadata;
import org.devgateway.toolkit.persistence.dto.ServiceDimension;
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
import java.util.Collection;
import java.util.List;

import static org.devgateway.toolkit.forms.client.ClientConstants.CODE_PREFIX;
import static org.devgateway.toolkit.forms.client.ClientConstants.JobStatus.COMPLETED;
import static org.devgateway.toolkit.forms.client.ClientConstants.JobStatus.ERROR;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.NOT_PUBLISHED;
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
        datasets.addAll(tetsimDatasetService.findAllPublishing());
        datasets.addAll(csvDatasetService.findAllPublishing());

        checkDatasetJobs(datasets);
    }

    private void checkDatasetJobs(List<Dataset> datasets) {
        datasets.forEach(d -> {
            ServiceMetadata serviceMetadata = eurekaClientService.findByName(d.getDestinationService());
            DatasetClient client = new DatasetClient(serviceMetadata.getUrl());
            String status = client.getDatasetJobStatus(CODE_PREFIX + d.getId()).getStatus();
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

    public void publishDataset(Dataset dataset, String fileName, byte[] content) throws DataSetClientException {
        String serviceURL = getDestinationService(dataset).getUrl();
        DatasetClient client = new DatasetClient(serviceURL);

        String name = "Dataset " + dataset.getYear();
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

    public List<ServiceDimension> getDimensions(final String serviceName) {
        ServiceMetadata service = eurekaClientService.findByName(serviceName);
        return new DatasetClient(service.getUrl()).getDimensions();
    }

    public ServiceDimension getDimensionById(final String serviceName, final long id) {
        ServiceMetadata service = eurekaClientService.findByName(serviceName);
        return new DatasetClient(service.getUrl()).getDimensionById(id);
    }

    public void updateDimension(final String serviceName, final ServiceDimension dimension) {
        ServiceMetadata service = eurekaClientService.findByName(serviceName);
        new DatasetClient(service.getUrl()).updateDimension(dimension);
    }

    public void addDimensionToService(final String service, final ServiceDimension serviceDimension) {
        ServiceMetadata serviceMetadata = eurekaClientService.findByName(service);
        new DatasetClient(serviceMetadata.getUrl()).addDimension(serviceDimension);
    }

    public List<ServiceMeasure> getMeasures(final String serviceName) {
        ServiceMetadata service = eurekaClientService.findByName(serviceName);
        return new DatasetClient(service.getUrl()).getMeasures();
    }

    public ServiceMeasure getMeasureById(final String serviceName, final long id) {
        ServiceMetadata service = eurekaClientService.findByName(serviceName);
        return new DatasetClient(service.getUrl()).getMeasureById(id);
    }

    public void updateMeasure(final String serviceName, final ServiceMeasure measure) {
        ServiceMetadata service = eurekaClientService.findByName(serviceName);
        new DatasetClient(service.getUrl()).updateMeasure(measure);
    }

    public void addMeasureToService(final String service, final ServiceMeasure serviceMeasure) {
        ServiceMetadata serviceMetadata = eurekaClientService.findByName(service);
        new DatasetClient(serviceMetadata.getUrl()).addMeasure(serviceMeasure);
    }

}
