package org.devgateway.toolkit.forms.client;

import org.devgateway.toolkit.persistence.dto.ServiceFilter;

import javax.ws.rs.core.GenericType;
import java.util.List;

public class ServiceFilterClient extends ServiceEntityClient<ServiceFilter> {

    public ServiceFilterClient(final String baseUrl) {
        super(baseUrl);
    }

    @Override
    public String getEntitiesPath() {
        return ClientConstants.PATH_FILTERS;
    }

    @Override
    protected GenericType<ServiceFilter> getGenericType() {
        return new GenericType<ServiceFilter>() {};
    }

    @Override
    protected GenericType<List<ServiceFilter>> getGenericListType() {
        return new GenericType<List<ServiceFilter>>() {};
    }

}
