package org.devgateway.toolkit.persistence.dao.data;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.categories.TobaccoProduct;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TetsimTobaccoProductValue extends AbstractAuditableEntity {

    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private TetsimPriceVariable priceVariable;

    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private TobaccoProduct product;

    private BigDecimal value;

    public TobaccoProduct getProduct() {
        return product;
    }

    public void setProduct(final TobaccoProduct product) {
        this.product = product;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(final BigDecimal value) {
        this.value = value;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return priceVariable;
    }
}
