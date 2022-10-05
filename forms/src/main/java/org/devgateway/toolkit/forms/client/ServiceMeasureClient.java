package org.devgateway.toolkit.forms.client;

import org.devgateway.toolkit.persistence.dto.ServiceMeasure;

import javax.ws.rs.core.GenericType;
import java.util.List;

public class ServiceMeasureClient extends ServiceEntityClient<ServiceMeasure> {

    public ServiceMeasureClient(final String baseUrl) {
        super(baseUrl);
    }

    @Override
    public String getEntitiesPath() {
        return ClientConstants.PATH_MEASURES;
    }

    @Override
    protected GenericType<ServiceMeasure> getGenericType() {
        return new GenericType<ServiceMeasure>() {};
    }

    @Override
    protected GenericType<List<ServiceMeasure>> getGenericListType() {
        return new GenericType<List<ServiceMeasure>>() {};
    }

}
