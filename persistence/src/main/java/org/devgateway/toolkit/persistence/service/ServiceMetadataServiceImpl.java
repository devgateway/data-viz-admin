package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.ServiceMetadata;
import org.devgateway.toolkit.persistence.repository.ServiceMetadataRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ServiceMetadataServiceImpl extends BaseJpaServiceImpl<ServiceMetadata> implements ServiceMetadataService {
    @Autowired
    private ServiceMetadataRepository serviceMetadataRepository;

    @Override
    public ServiceMetadata findByName(final String name) {
        return serviceMetadataRepository.findByName(name);
    }

    @Override
    protected BaseJpaRepository<ServiceMetadata, Long> repository() {
        return serviceMetadataRepository;
    }

    @Override
    public ServiceMetadata newInstance() {
        return new ServiceMetadata();
    }
}
