package org.devgateway.toolkit.persistence.dao.data;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.ServiceMetadata;
import org.hibernate.envers.Audited;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@MappedSuperclass
public abstract class Dataset extends AbstractStatusAuditableEntity implements Serializable {

    @NotNull
    @Audited
    private Integer year;

    @ManyToOne
    @Audited
    private ServiceMetadata service;

    public ServiceMetadata getService() {
        return service;
    }

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

    public void setService(final ServiceMetadata service) {
        this.service = service;
    }
}
