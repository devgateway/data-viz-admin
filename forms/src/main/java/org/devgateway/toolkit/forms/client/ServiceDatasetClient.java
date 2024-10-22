package org.devgateway.toolkit.forms.client;

import org.devgateway.toolkit.persistence.dto.ServiceDataset;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.Family.SUCCESSFUL;

public class ServiceDatasetClient extends ServiceEntityClient<ServiceDataset> {

    public ServiceDatasetClient(final String baseUrl) {
        super(baseUrl);
    }

    @Override
    public String getEntitiesPath() {
        return ClientConstants.PATH_DATASETS;
    }

    @Override
    protected GenericType<ServiceDataset> getGenericType() {
        return new GenericType<ServiceDataset>() {};
    }

    @Override
    protected GenericType<List<ServiceDataset>> getGenericListType() {
        return new GenericType<List<ServiceDataset>>() {};
    }

    public void delete(final ServiceDataset entity) {
        if (isUp()) {
            Response response = client.target(baseUrl)
                    .path(getEntitiesPath())
                    .path(entity.getCode().toString())
                    .request(APPLICATION_JSON)
                    .delete();

            if (response.getStatusInfo().getFamily() != SUCCESSFUL) {
                throw new RuntimeException("Error in deleting the dataset");
            }
        } else {
            throw new RuntimeException(("Service is not up"));
        }
    }

}
