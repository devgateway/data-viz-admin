package org.devgateway.toolkit.persistence.dao;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
public class ServiceMetadata extends AbstractStatusAuditableEntity implements Serializable {

    @NotNull
    @Audited
    private String name;

    @Audited
    private String description;

    @Audited
    private String url;

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }
}
