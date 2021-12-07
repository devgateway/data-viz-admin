package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.categories.TobaccoProduct;
import org.devgateway.toolkit.persistence.dao.categories.TobaccoProduct_;
import org.devgateway.toolkit.persistence.repository.SpecificationContext;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class TobaccoProductFilterState extends JpaFilterState<TobaccoProduct> {

    private static final long serialVersionUID = 6916178929560942055L;

    private String label;

    @Override
    public Specification<TobaccoProduct> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            SpecificationContext<TobaccoProduct> sc = new SpecificationContext<>(root, query, cb);

            if (StringUtils.isNotBlank(label)) {
                predicates.add(cb.like(root.get(TobaccoProduct_.label), "%" + label + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }
}
