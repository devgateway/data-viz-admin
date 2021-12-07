package org.devgateway.toolkit.persistence.service.data;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.repository.data.TetsimDatasetRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CacheConfig(cacheNames = "servicesCache")
@Transactional(readOnly = true)
public class TetsimDatasetServiceImpl extends BaseJpaServiceImpl<TetsimDataset> implements TetsimDatasetService {

    @Autowired
    private TetsimDatasetRepository tetsimDatasetRepository;

    @Override
    public TetsimDataset newInstance() {
        return new TetsimDataset();
    }

    @Override
    protected BaseJpaRepository<TetsimDataset, Long> repository() {
        return tetsimDatasetRepository;
    }

}
