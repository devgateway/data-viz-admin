package org.devgateway.toolkit.forms.client;

import org.devgateway.toolkit.persistence.dto.ServiceCategory;

import jakarta.ws.rs.core.GenericType;
import java.util.List;

public class ServiceCategoryClient extends ServiceEntityClient<ServiceCategory> {

    public ServiceCategoryClient(final String baseUrl) {
        super(baseUrl);
    }

    @Override
    public String getEntitiesPath() {
        return ClientConstants.PATH_CATEGORIES;
    }

    @Override
    protected GenericType<ServiceCategory> getGenericType() {
        return new GenericType<ServiceCategory>() {};
    }

    @Override
    protected GenericType<List<ServiceCategory>> getGenericListType() {
        return new GenericType<List<ServiceCategory>>() {};
    }

}
