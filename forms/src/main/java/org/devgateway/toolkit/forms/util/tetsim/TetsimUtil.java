package org.devgateway.toolkit.forms.util.tetsim;

import org.devgateway.toolkit.persistence.dao.data.TetsimPriceVariable;

import java.math.BigDecimal;

public final class TetsimUtil {

    private TetsimUtil() {

    }

    public static BigDecimal getTobaccoProductValueFromVariable(TetsimPriceVariable variable,
                                                                String tobaccoProductLabel) {
        return variable.getValues().stream()
                .filter(t -> t.getProduct().getLabel().equalsIgnoreCase(tobaccoProductLabel))
                .findFirst().get().getValue();
    }
}
