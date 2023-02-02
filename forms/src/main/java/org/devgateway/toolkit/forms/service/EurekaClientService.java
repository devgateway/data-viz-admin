package org.devgateway.toolkit.forms.service;

import com.netflix.appinfo.InstanceInfo;
import org.devgateway.toolkit.persistence.dto.ServiceMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.devgateway.toolkit.forms.WebConstants.SERVICE_DATA_TYPE;

@Service
public class EurekaClientService {

    @Autowired
    private DiscoveryClient discoveryClient;

    public List<ServiceMetadata> findAll() {
        List<ServiceMetadata> services = new ArrayList<>();
        discoveryClient.getServices().forEach(s -> {
            discoveryClient.getInstances(s).stream().forEach(instance -> {
                InstanceInfo instanceInfo = ((EurekaServiceInstance) instance).getInstanceInfo();
                ServiceMetadata service = new ServiceMetadata();
                service.setName(instanceInfo.getAppName());
                service.setUrl(instanceInfo.getHomePageUrl());
                service.setId(instanceInfo.getId());
                service.setType(instance.getMetadata().getOrDefault("type", null));
                service.setLabel(instance.getMetadata().getOrDefault("label", instanceInfo.getAppName()));
                service.setStatus(instanceInfo.getStatus().toString());
                service.setTetsim(Boolean.valueOf(instanceInfo.getMetadata().getOrDefault("tetsim", "false")));
                services.add(service);
            });
        });

        return services;
    }

    public List<ServiceMetadata> findAllWithData() {
        return findAll().stream()
                .filter(s -> SERVICE_DATA_TYPE.equalsIgnoreCase(s.getType()))
                .collect(Collectors.toList());
    }

    public ServiceMetadata findByName(String name) {
        return findAll().stream()
                .filter(s -> s.getName().equals(name))
                .findFirst().get();
    }

}
