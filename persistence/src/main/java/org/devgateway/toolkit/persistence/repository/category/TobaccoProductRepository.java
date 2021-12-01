package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.TobaccoProduct;
import org.devgateway.toolkit.persistence.repository.norepository.UniquePropertyRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TobaccoProductRepository extends CategoryRepository<TobaccoProduct>,
        UniquePropertyRepository<TobaccoProduct, Long> {

}
