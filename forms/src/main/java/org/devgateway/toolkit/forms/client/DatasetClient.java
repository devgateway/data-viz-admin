package org.devgateway.toolkit.forms.client;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;


public class DatasetClient {

    private final JerseyClient client;

    private final String baseUrl;

    private final static String PATH_HEALTH = "actuator/health";

    private final static String PATH_JOBS = "admin/jobs/";

    public DatasetClient(final String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = JerseyClientBuilder.createClient();
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

    public boolean isUp() {
        Response healthResponse = client.target(baseUrl).path(PATH_HEALTH)
                .request(APPLICATION_JSON).get();

        if (healthResponse.getStatusInfo().getFamily() == SUCCESSFUL) {
            return healthResponse.readEntity(ServiceHealthStatus.class).isUp();
        }

        return false;
    }

//    List<Map<String, Object>> response = client.resource(uri)
//            .type(MediaType.APPLICATION_JSON_TYPE)
//            .accept(MediaType.APPLICATION_JSON_TYPE)
//            .post(new GenericType<List<Map<String, Object>>>() { }, jsonActivities);
//
//    List<Long> queueIds = new ArrayList<>();
}
