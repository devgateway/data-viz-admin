package org.devgateway.toolkit.persistence.tetsim.builder;

import org.devgateway.toolkit.persistence.dao.categories.TobaccoProduct;
import org.devgateway.toolkit.persistence.dao.data.TetsimTobaccoProductValue;

import java.math.BigDecimal;

public class TetsimTobaccoProductValueBuilder {

    private TetsimTobaccoProductValue productValue;

    public TetsimTobaccoProductValueBuilder() {
        this.productValue = new TetsimTobaccoProductValue();
    }

    public TetsimTobaccoProductValueBuilder withTobaccoProduct(TobaccoProduct tobaccoProduct) {
        productValue.setProduct(tobaccoProduct);
        return this;
    }

    public TetsimTobaccoProductValueBuilder withValue(BigDecimal value) {
        productValue.setValue(value);
        return this;
    }

    public TetsimTobaccoProductValue build() {
        return productValue;
    }
    
}
