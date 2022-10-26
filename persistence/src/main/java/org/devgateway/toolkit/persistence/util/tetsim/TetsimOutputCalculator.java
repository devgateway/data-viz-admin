package org.devgateway.toolkit.persistence.util.tetsim;

import org.devgateway.toolkit.persistence.dao.data.TobaccoProduct;
import org.devgateway.toolkit.persistence.dto.TetsimOutput;

public interface TetsimOutputCalculator {

    TetsimOutput calculate(TobaccoProduct tobaccoProduct);

}
