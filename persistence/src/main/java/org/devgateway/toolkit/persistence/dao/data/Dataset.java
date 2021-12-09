package org.devgateway.toolkit.persistence.dao.data;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.Person;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@MappedSuperclass
public abstract class Dataset extends AbstractAuditableEntity implements Serializable {

    @NotNull
    @Audited
    private Integer year;

    @NotNull
    @Audited
    @Enumerated(EnumType.STRING)
    private DatasetStatus status;

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

    public DatasetStatus getStatus() {
        return status;
    }

    public void setStatus(final DatasetStatus status) {
        this.status = status;
    }

}
