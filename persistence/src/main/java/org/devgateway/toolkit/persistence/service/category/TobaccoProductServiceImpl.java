package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.TobaccoProduct;
import org.devgateway.toolkit.persistence.repository.category.GroupRepository;
import org.devgateway.toolkit.persistence.repository.category.TobaccoProductRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.repository.norepository.UniquePropertyRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CacheConfig(cacheNames = "servicesCache")
@Transactional(readOnly = true)
public class TobaccoProductServiceImpl extends BaseJpaServiceImpl<TobaccoProduct> implements TobaccoProductService {

    @Autowired
    private TobaccoProductRepository tobaccoProductRepository;

    @Override
    public TobaccoProduct newInstance() {
        return new TobaccoProduct();
    }

    @Override
    protected BaseJpaRepository<TobaccoProduct, Long> repository() {
        return tobaccoProductRepository;
    }

    @Override
    public TextSearchableRepository<TobaccoProduct, Long> textRepository() {
        return tobaccoProductRepository;
    }

    @Override
    public UniquePropertyRepository<TobaccoProduct, Long> uniquePropertyRepository() {
        return tobaccoProductRepository;
    }
}
