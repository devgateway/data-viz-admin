package org.devgateway.toolkit.forms.service.admin;

import org.devgateway.toolkit.forms.client.ServiceEntityClient;
import org.devgateway.toolkit.forms.client.ServiceMeasureClient;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.persistence.dto.ServiceMeasure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceMeasureServiceImpl extends BaseServiceEntityServiceImpl<ServiceMeasure> implements ServiceMeasureService {

    @Autowired
    private EurekaClientService eurekaClientService;

    @Override
    public ServiceMeasure newInstance() {
        return new ServiceMeasure();
    }

    @Override
    protected ServiceEntityClient<ServiceMeasure> serviceEntityClient(final String serviceName) {
        return new ServiceMeasureClient(eurekaClientService.findByName(serviceName).getUrl());
    }
}
