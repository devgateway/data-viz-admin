package org.devgateway.toolkit.persistence.dao.data;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TetsimPriceVariable extends AbstractAuditableEntity  {

    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private TetsimDataset dataset;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TetsimTobaccoProductValue> values;

    @Override
    public AbstractAuditableEntity getParent() {
        return dataset;
    }

    public Set<TetsimTobaccoProductValue> getValues() {
        return values;
    }

    public void setValues(final Set<TetsimTobaccoProductValue> values) {
        this.values = values;
    }
}
