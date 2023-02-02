package org.devgateway.toolkit.forms.service.admin;

import org.devgateway.toolkit.forms.client.ServiceDatasetClient;
import org.devgateway.toolkit.forms.client.ServiceEntityClient;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.persistence.dto.ServiceDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceDatasetServiceImpl extends BaseServiceEntityServiceImpl<ServiceDataset> implements ServiceDatasetService {

    @Autowired
    private EurekaClientService eurekaClientService;

    @Override
    public ServiceDataset newInstance() {
        return new ServiceDataset();
    }

    @Override
    protected ServiceEntityClient<ServiceDataset> serviceEntityClient(final String serviceName) {
        return new ServiceDatasetClient(eurekaClientService.findByName(serviceName).getUrl());
    }
}
