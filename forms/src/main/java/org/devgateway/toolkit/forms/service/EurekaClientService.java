package org.devgateway.toolkit.forms.service;

import com.netflix.appinfo.InstanceInfo;
import org.devgateway.toolkit.persistence.dto.ServiceMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                service.setStatus(instanceInfo.getStatus().toString());
                services.add(service);
            });
        });

        return services;
    }

    public List<ServiceMetadata> findAllWithData() {
        return findAll().stream()
                .filter(s -> "data".equals(s.getType()))
                .collect(Collectors.toList());
    }


    public Set<String> getServiceByMetadataType(String type) {
        Set<String> services = new HashSet<>();
        discoveryClient.getServices().forEach(s -> {
            System.out.print(s + "-");
            discoveryClient.getInstances(s).stream().forEach(instance -> {
                if ("data".equals(instance.getMetadata().getOrDefault("type", ""))) {
                    services.add(s);
                }
            });
        });

        return services;
    }

    public ServiceMetadata getServiceByName(String name) {
        return findAll().stream()
                .filter(s -> s.getName().equals(name))
                .findFirst().get();
    }

}
