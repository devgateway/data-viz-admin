package org.devgateway.toolkit.persistence.dao.data;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TetsimPriceVariable extends AbstractAuditableEntity {

    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private TetsimDataset dataset;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TetsimTobaccoProductValue> values = new HashSet<>();

    public TetsimPriceVariable() {
    }

    public TetsimPriceVariable(final TetsimDataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return dataset;
    }

    public TetsimDataset getDataset() {
        return dataset;
    }

    public void setDataset(final TetsimDataset dataset) {
        this.dataset = dataset;
    }

    public Set<TetsimTobaccoProductValue> getValues() {
        return values;
    }

    public void setValues(final Set<TetsimTobaccoProductValue> values) {
        this.values = values;
    }
}
