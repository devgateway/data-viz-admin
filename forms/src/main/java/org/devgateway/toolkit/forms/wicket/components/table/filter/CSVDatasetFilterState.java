package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity_;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.repository.SpecificationContext;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DELETED;

/**
 * Created by Viorel Chihai
 */
public class CSVDatasetFilterState extends JpaFilterState<CSVDataset> {

    private static final long serialVersionUID = -3419455862012018431L;

    @Override
    public Specification<CSVDataset> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            SpecificationContext<CSVDataset> sc = new SpecificationContext<>(root, query, cb);

            predicates.add(sc.cb().notLike(sc.root().get(AbstractStatusAuditableEntity_.status), DELETED));

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

}