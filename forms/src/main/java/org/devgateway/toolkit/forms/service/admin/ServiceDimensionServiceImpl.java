package org.devgateway.toolkit.forms.service.admin;

import org.devgateway.toolkit.forms.client.ServiceDimensionClient;
import org.devgateway.toolkit.forms.client.ServiceEntityClient;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.persistence.dto.ServiceDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceDimensionServiceImpl extends BaseServiceEntityServiceImpl<ServiceDimension> implements ServiceDimensionService {

    @Autowired
    private EurekaClientService eurekaClientService;

    @Override
    public ServiceDimension newInstance() {
        return new ServiceDimension();
    }

    @Override
    protected ServiceEntityClient<ServiceDimension> serviceEntityClient(final String serviceName) {
        return new ServiceDimensionClient(eurekaClientService.findByName(serviceName).getUrl());
    }
}
