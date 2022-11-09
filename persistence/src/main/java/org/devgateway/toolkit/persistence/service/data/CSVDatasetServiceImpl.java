package org.devgateway.toolkit.persistence.service.data;

import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.repository.data.CSVDatasetRepository;
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
public class CSVDatasetServiceImpl extends BaseJpaServiceImpl<CSVDataset> implements CSVDatasetService {

    @Autowired
    private CSVDatasetRepository csvDatasetRepository;

    @Override
    public CSVDataset newInstance() {
        return new CSVDataset();
    }

    @Override
    protected BaseJpaRepository<CSVDataset, Long> repository() {
        return csvDatasetRepository;
    }

    @Override
    public UniquePropertyRepository<CSVDataset, Long> uniquePropertyRepository() {
        return csvDatasetRepository;
    }

    @Override
    public List<CSVDataset> findAllDeleted() {
        return csvDatasetRepository.findAllDeleted();
    }

    @Override
    public List<CSVDataset> findAllInProgress() {
        return csvDatasetRepository.findAllInProgress();
    }

    @Override
    public long countByNonPublished(final Integer year) {
        return csvDatasetRepository.countByNonPublished(year);
    }

}
