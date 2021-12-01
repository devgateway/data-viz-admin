package org.devgateway.toolkit.persistence.dao.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.Person;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Entity
public class Dataset extends AbstractAuditableEntity implements Serializable, Labelable {

    private String label;

    @Column(length = 1024)
    private String description;

    private Integer year;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<FileMetadata> files;

    private boolean approved;

    @ManyToOne(fetch = FetchType.EAGER)
    private Person uploadedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private Person approvedBy;

    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private String dtype;

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    @Override
    public void setLabel(final String label) {

    }

    @Override
    public String getLabel() {
        return null;
    }
}
