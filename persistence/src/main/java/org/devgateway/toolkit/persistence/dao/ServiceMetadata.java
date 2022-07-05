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

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return getName();
    }
}
