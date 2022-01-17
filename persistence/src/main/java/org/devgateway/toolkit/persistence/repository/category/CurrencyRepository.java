package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.Currency;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CurrencyRepository extends CategoryRepository<Currency> {

}
