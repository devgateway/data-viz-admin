package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity_;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset_;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.repository.SpecificationContext;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DELETED;

/**
 * Created by Viorel Chihai
 */
public class TetsimDatasetFilterState extends JpaFilterState<TetsimDataset> {

    private static final long serialVersionUID = 6655165533257383853L;

    private String service;

    @Override
    public Specification<TetsimDataset> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            SpecificationContext<TetsimDataset> sc = new SpecificationContext<>(root, query, cb);

            if (StringUtils.isNotBlank(service)) {
                predicates.add(sc.cb().equal(sc.cb().lower(sc.root().get(CSVDataset_.destinationService)), service.toLowerCase()));
            }

            predicates.add(sc.cb().notLike(sc.root().get(AbstractStatusAuditableEntity_.status), DELETED));

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public String getService() {
        return service;
    }

    public void setService(final String service) {
        this.service = service;
    }
}