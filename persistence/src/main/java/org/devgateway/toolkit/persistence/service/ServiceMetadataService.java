package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.ServiceMetadata;

public interface ServiceMetadataService extends BaseJpaService<ServiceMetadata> {

    ServiceMetadata findByName(String name);
}
