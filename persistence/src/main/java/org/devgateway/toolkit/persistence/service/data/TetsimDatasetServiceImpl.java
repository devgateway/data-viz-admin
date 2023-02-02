package org.devgateway.toolkit.persistence.service.data;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.repository.data.TetsimDatasetRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.UniquePropertyRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public UniquePropertyRepository<TetsimDataset, Long> uniquePropertyRepository() {
        return tetsimDatasetRepository;
    }

    @Override
    public List<TetsimDataset> findAllDeleted() {
        return tetsimDatasetRepository.findAllDeleted();
    }

    @Override
    public List<TetsimDataset> findAllInProgress() {
        return tetsimDatasetRepository.findAllInProgress();
    }

    @Override
    public long countByNonPublished(final Integer year) {
        return tetsimDatasetRepository.countByNonPublished(year);
    }

    @Override
    public List<TetsimDataset> findAllNotDeletedForService(final String service) {
        return tetsimDatasetRepository.findAllNotDeletedForService(service);
    }

}
