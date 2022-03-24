package org.devgateway.toolkit.persistence.tetsim.builder;

import org.devgateway.toolkit.persistence.dao.categories.TobaccoProduct;

public class TobaccoProductBuilder {

    private TobaccoProduct product;

    public TobaccoProductBuilder() {
        this.product = new TobaccoProduct();
    }

    public TobaccoProductBuilder withLabel(String label) {
        this.product.setLabel(label);
        return this;
    }

    public TobaccoProductBuilder withIllicit(Boolean illicit) {
        this.product.setIllicit(illicit);
        return this;
    }

    public TobaccoProduct build() {
        return this.product;
    }
}
