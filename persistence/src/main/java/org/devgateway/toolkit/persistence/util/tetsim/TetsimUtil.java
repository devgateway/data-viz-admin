package org.devgateway.toolkit.persistence.util.tetsim;

import org.devgateway.toolkit.persistence.dao.data.TetsimPriceVariable;
import org.devgateway.toolkit.persistence.dao.data.TobaccoProduct;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.ILLICIT;

public final class TetsimUtil {

    private TetsimUtil() {

    }

    public static BigDecimal getTobaccoProductValueFromVariable(TetsimPriceVariable variable,
                                                                TobaccoProduct tobaccoProduct) {
        return variable.getValues().stream()
                .filter(t -> t.getProduct().equals(tobaccoProduct))
                .findFirst().get().getValue();
    }

    public static List<TobaccoProduct> getLegalTobaccoProducts() {
        return Arrays.asList(TobaccoProduct.values())
                .stream()
                .filter(tobaccoProduct -> !tobaccoProduct.equals(ILLICIT))
                .collect(Collectors.toList());
    }


}
