package org.devgateway.toolkit.persistence.dao.data;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.Person;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class Dataset extends AbstractAuditableEntity implements Serializable, Labelable {

    private String label;

    @Column(length = 1024)
    private String description;

    private Integer year;

    private boolean approved;

    @ManyToOne(fetch = FetchType.EAGER)
    private Person uploadedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private Person approvedBy;

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    @Override
    public void setLabel(final String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
