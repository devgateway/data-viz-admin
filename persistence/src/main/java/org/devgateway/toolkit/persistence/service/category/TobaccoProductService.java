package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.TobaccoProduct;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;
import org.devgateway.toolkit.persistence.service.UniquePropertyService;

public interface TobaccoProductService extends BaseJpaService<TobaccoProduct>, TextSearchableService<TobaccoProduct>,
        UniquePropertyService<TobaccoProduct> {
}
