package org.devgateway.toolkit.persistence.repository;

import org.devgateway.toolkit.persistence.dao.ServiceMetadata;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ServiceMetadataRepository extends BaseJpaRepository<ServiceMetadata, Long> {

    ServiceMetadata findByName(String name);

}
