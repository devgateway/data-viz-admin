package org.devgateway.toolkit.forms.client;

import org.devgateway.toolkit.persistence.dto.ServiceDimension;

import jakarta.ws.rs.core.GenericType;
import java.util.List;

public class ServiceDimensionClient extends ServiceEntityClient<ServiceDimension> {

    public ServiceDimensionClient(final String baseUrl) {
        super(baseUrl);
    }

    @Override
    public String getEntitiesPath() {
        return ClientConstants.PATH_DIMENSIONS;
    }

    @Override
    protected GenericType<ServiceDimension> getGenericType() {
        return new GenericType<ServiceDimension>() {};
    }

    @Override
    protected GenericType<List<ServiceDimension>> getGenericListType() {
        return new GenericType<List<ServiceDimension>>() {};
    }

}
