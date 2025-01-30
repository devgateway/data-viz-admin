package org.devgateway.toolkit.persistence.dao.data;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.hibernate.envers.Audited;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@MappedSuperclass
public abstract class Dataset extends AbstractStatusAuditableEntity implements Serializable {

    @NotNull
    @Audited
    private Integer year;

    @Audited
    private String destinationService;

    public abstract String getDescription();

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    public void setDestinationService(final String destinationService) {
        this.destinationService = destinationService;
    }

    public String getDestinationService() {
        return destinationService;
    }
}
