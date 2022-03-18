package org.devgateway.toolkit.forms.util.tetsim.builder;

import org.devgateway.toolkit.persistence.dao.data.TetsimPriceVariable;
import org.devgateway.toolkit.persistence.dao.data.TetsimTobaccoProductValue;

public class TetsimPriceVariableBuilder {

    private TetsimPriceVariable priceVariable;

    public TetsimPriceVariableBuilder() {
        this.priceVariable = new TetsimPriceVariable();
    }

    public TetsimPriceVariableBuilder add(TetsimTobaccoProductValue tetsimTobaccoProductValue) {
        priceVariable.getValues().add(tetsimTobaccoProductValue);
        return this;
    }

    public TetsimPriceVariable build() {
        return priceVariable;
    }

}
