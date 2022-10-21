package org.devgateway.toolkit.persistence.dao.data;

import java.util.EnumSet;
import java.util.List;

public enum TobaccoProduct {

    IMPORTED("Imported"),
    PREMIUM("Premium"),
    POPULAR("Popular"),
    DISCOUNT("Discount"),
    ILLICIT("Illicit", true);

    public static final EnumSet<TobaccoProduct> ALL = EnumSet.of(IMPORTED, PREMIUM, POPULAR, DISCOUNT, ILLICIT);

    private final String label;
    private final Boolean illicit;

    TobaccoProduct(String label) {
        this(label, false);
    }

    TobaccoProduct(String label, Boolean illicit) {
        this.label = label;
        this.illicit = illicit;
    }

    public String getLabel() {
        return label;
    }

    public Boolean isIllicit() {
        return illicit;
    }
}
