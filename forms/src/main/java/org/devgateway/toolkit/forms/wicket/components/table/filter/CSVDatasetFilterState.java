package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity_;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset_;
import org.devgateway.toolkit.persistence.repository.SpecificationContext;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DELETED;

/**
 * Created by Viorel Chihai
 */
public class CSVDatasetFilterState extends JpaFilterState<CSVDataset> {

    private static final long serialVersionUID = -3419455862012018431L;

    private Integer year;

    private String status;

    private String service;

    @Override
    public Specification<CSVDataset> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            SpecificationContext<CSVDataset> sc = new SpecificationContext<>(root, query, cb);

            predicates.add(sc.cb().notLike(sc.root().get(AbstractStatusAuditableEntity_.status), DELETED));

            if (StringUtils.isNotBlank(service)) {
                predicates.add(sc.cb().equal(sc.cb().lower(sc.root().get(CSVDataset_.destinationService)), service.toLowerCase()));
            }

            if (year != null) {
                predicates.add(sc.cb().equal(sc.root().get(CSVDataset_.year), year));
            }

            if (StringUtils.isNotBlank(status)) {
                predicates.add(sc.cb().like(sc.root().get(CSVDataset_.status), status));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(final String service) {
        this.service = service;
    }
}