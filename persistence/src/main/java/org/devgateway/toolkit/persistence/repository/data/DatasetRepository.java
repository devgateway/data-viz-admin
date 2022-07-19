package org.devgateway.toolkit.persistence.repository.data;

import org.devgateway.toolkit.persistence.dao.data.Dataset;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface DatasetRepository<T extends Dataset> extends BaseJpaRepository<T, Long> {

}
