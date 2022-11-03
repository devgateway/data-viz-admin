package org.devgateway.toolkit.forms.service.admin;

import org.devgateway.toolkit.forms.client.ServiceEntityClient;
import org.devgateway.toolkit.forms.client.ServiceFilterClient;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.persistence.dto.ServiceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceFilterServiceImpl extends BaseServiceEntityServiceImpl<ServiceFilter> implements ServiceFilterService {

    @Autowired
    private EurekaClientService eurekaClientService;

    @Override
    public ServiceFilter newInstance() {
        return new ServiceFilter();
    }

    @Override
    protected ServiceEntityClient<ServiceFilter> serviceEntityClient(final String serviceName) {
        return new ServiceFilterClient(eurekaClientService.findByName(serviceName).getUrl());
    }
}
