package org.devgateway.toolkit.persistence.repository.data;

import org.devgateway.toolkit.persistence.dao.data.Dataset;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DatasetRepository<T extends Dataset> extends BaseJpaRepository<T, Long> {

}
