package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Currency;
import org.devgateway.toolkit.persistence.repository.category.CurrencyRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CacheConfig(cacheNames = "servicesCache")
@Transactional(readOnly = true)
public class CurrencyServiceImpl extends BaseJpaServiceImpl<Currency> implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public Currency newInstance() {
        return new Currency();
    }

    @Override
    protected BaseJpaRepository<Currency, Long> repository() {
        return currencyRepository;
    }

    @Override
    public TextSearchableRepository<Currency, Long> textRepository() {
        return currencyRepository;
    }

    @Override
    public Sort getSort() {
        return Sort.by(Sort.Direction.ASC, "label");
    }

}
