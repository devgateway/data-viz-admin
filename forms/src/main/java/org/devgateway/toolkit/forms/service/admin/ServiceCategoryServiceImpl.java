package org.devgateway.toolkit.forms.service.admin;

import org.devgateway.toolkit.forms.client.ServiceCategoryClient;
import org.devgateway.toolkit.forms.client.ServiceEntityClient;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.persistence.dto.ServiceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceCategoryServiceImpl extends BaseServiceEntityServiceImpl<ServiceCategory> implements ServiceCategoryService {

    @Autowired
    private EurekaClientService eurekaClientService;

    @Override
    public ServiceCategory newInstance() {
        return new ServiceCategory();
    }

    @Override
    protected ServiceEntityClient<ServiceCategory> serviceEntityClient(final String serviceName) {
        return new ServiceCategoryClient(eurekaClientService.findByName(serviceName).getUrl());
    }
}
