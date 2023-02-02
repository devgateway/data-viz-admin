package org.devgateway.toolkit.forms.client;

import org.apache.commons.io.FileUtils;
import org.devgateway.toolkit.persistence.dao.data.Dataset;
import org.devgateway.toolkit.persistence.dto.ServiceDimension;
import org.devgateway.toolkit.persistence.dto.ServiceMeasure;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM_TYPE;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA_TYPE;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static org.devgateway.toolkit.forms.client.ClientConstants.CODE_PREFIX;
import static org.devgateway.toolkit.forms.client.ClientConstants.PATH_CODE;
import static org.devgateway.toolkit.forms.client.ClientConstants.PATH_DATASETS;
import static org.devgateway.toolkit.forms.client.ClientConstants.PATH_DIMENSIONS;
import static org.devgateway.toolkit.forms.client.ClientConstants.PATH_HEALTH;
import static org.devgateway.toolkit.forms.client.ClientConstants.PATH_JOBS;
import static org.devgateway.toolkit.forms.client.ClientConstants.PATH_MEASURES;
import static org.devgateway.toolkit.forms.client.ClientConstants.PATH_TEMPLATE_DOWNLOAD;


public class DatasetClient {

    private final JerseyClient client;

    private final String baseUrl;

    public DatasetClient(final String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = JerseyClientBuilder.createClient().register(MultiPartFeature.class);
    }

    public DatasetJobStatus getDatasetStatus(String jobId) throws DataSetClientException {
        if (isUp()) {
            Response jobStatusResponse = client.target(baseUrl)
                    .path(PATH_JOBS)
                    .path(jobId)
                    .request(APPLICATION_JSON)
                    .get();

            if (jobStatusResponse.getStatusInfo().getFamily() == SUCCESSFUL) {
                return jobStatusResponse.readEntity(DatasetJobStatus.class);
            }

            throw new DataSetClientException(jobStatusResponse.toString());
        }

        throw new RuntimeException(("Service is not up"));
    }

    public DatasetJobStatus unpublishDataset(String code) throws DataSetClientException {
        if (isUp()) {
            Response jobStatusResponse = client.target(baseUrl)
                    .path(PATH_DATASETS)
                    .path(code)
                    .request()
                    .delete();

            if (jobStatusResponse.getStatusInfo().getFamily() == SUCCESSFUL) {
                return jobStatusResponse.readEntity(DatasetJobStatus.class);
            }

            throw new DataSetClientException(jobStatusResponse.toString());
        } else {
            throw new RuntimeException(("Service is not up"));
        }
    }

    public DatasetJobStatus publishDataset(Dataset dataset, byte[] datasetContent) throws DataSetClientException {
        if (isUp()) {
            File tempUploadFile;
            try {
                tempUploadFile = File.createTempFile(dataset.getYear() + "_tetsim", "csv");
                tempUploadFile.deleteOnExit();

                FileUtils.writeByteArrayToFile(tempUploadFile, datasetContent);
                FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", tempUploadFile, APPLICATION_OCTET_STREAM_TYPE);
                fileDataBodyPart.setContentDisposition(FormDataContentDisposition.name("file").fileName(tempUploadFile.getName()).build());

                FormDataMultiPart multiPart = new FormDataMultiPart();
                multiPart.field("name", "TETSIM dataset " + dataset.getYear());
                multiPart.field("code", CODE_PREFIX + dataset.getId());
                multiPart.field("file", tempUploadFile.getName(), TEXT_PLAIN_TYPE)
                        .bodyPart(fileDataBodyPart);

                Response jobStatusResponse = client.target(baseUrl)
                        .path(PATH_DATASETS)
                        .request()
                        .post(Entity.entity(multiPart, MULTIPART_FORM_DATA_TYPE));

                if (jobStatusResponse.getStatusInfo().getFamily() == SUCCESSFUL) {
                    return jobStatusResponse.readEntity(DatasetJobStatus.class);
                }

                throw new DataSetClientException(jobStatusResponse.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException(("Service is not up"));
    }

    public DatasetJobStatus publishDataset(String name, String code, File file) throws DataSetClientException {
        if (isUp()) {
            FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", file, APPLICATION_OCTET_STREAM_TYPE);
            fileDataBodyPart.setContentDisposition(FormDataContentDisposition.name("file")
                    .fileName(file.getName())
                    .build());

            FormDataMultiPart multiPart = new FormDataMultiPart();
            multiPart.field("name", name);
            multiPart.field("code", code);
            multiPart.field("file", file.getName(), TEXT_PLAIN_TYPE).bodyPart(fileDataBodyPart);

            Response jobStatusResponse = client.target(baseUrl)
                    .path(PATH_DATASETS)
                    .request()
                    .post(Entity.entity(multiPart, MULTIPART_FORM_DATA_TYPE));

            if (jobStatusResponse.getStatusInfo().getFamily() == SUCCESSFUL) {
                return jobStatusResponse.readEntity(DatasetJobStatus.class);
            }

            throw new DataSetClientException(jobStatusResponse.toString());
        }

        throw new RuntimeException(("Service is not up"));
    }

    public DatasetJobStatus getDatasetJobStatus(String code) {
        Response jobStatusResponse = client.target(baseUrl)
                .path(PATH_JOBS)
                .path(PATH_CODE)
                .path(code)
                .request(APPLICATION_JSON).get();

        if (jobStatusResponse.getStatusInfo().getFamily() == SUCCESSFUL) {
            return jobStatusResponse.readEntity(DatasetJobStatus.class);
        }

        return null;
    }

    public boolean isUp() {
        Response healthResponse = client.target(baseUrl).path(PATH_HEALTH)
                .request(APPLICATION_JSON).get();

        if (healthResponse.getStatusInfo().getFamily() == SUCCESSFUL) {
            return healthResponse.readEntity(ServiceHealthStatus.class).isUp();
        }

        return false;
    }

    public List<ServiceDimension> getDimensions() {
        if (isUp()) {
            Response dimensionsResponse = client.target(baseUrl)
                    .path(PATH_DIMENSIONS)
                    .request(APPLICATION_JSON).get();

            if (dimensionsResponse.getStatusInfo().getFamily() == SUCCESSFUL) {
                return dimensionsResponse.readEntity(new GenericType<List<ServiceDimension>>() {});
            }

            return null;
        }

        throw new RuntimeException(("Service is not up"));
    }

    public List<ServiceMeasure> getMeasures() {
        if (isUp()) {
            Response measuresResponse = client.target(baseUrl)
                    .path(PATH_MEASURES)
                    .request(APPLICATION_JSON).get();

            if (measuresResponse.getStatusInfo().getFamily() == SUCCESSFUL) {
                return measuresResponse.readEntity(new GenericType<List<ServiceMeasure>>() {});
            }

            return null;
        }

        throw new RuntimeException(("Service is not up"));
    }

    public byte[] getTemplateDownload() {
        if (isUp()) {
            Response response = client.target(baseUrl)
                    .path(PATH_TEMPLATE_DOWNLOAD)
                    .request(APPLICATION_OCTET_STREAM)
                    .get();

            if (response.getStatusInfo().getFamily() == SUCCESSFUL) {
                return response.readEntity(byte[].class);
            }

            return null;
        }

        throw new RuntimeException(("Service is not up"));
    }
}
