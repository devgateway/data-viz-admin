package org.devgateway.toolkit.persistence.util.tetsim;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dao.data.TobaccoProduct;

import java.math.BigDecimal;

import static org.devgateway.toolkit.persistence.util.tetsim.TetsimUtil.getTobaccoProductValueFromVariable;

/**
 * @author vchihai
 */
public class TetsimOutputPerfectshiftCalculator extends TetsimOutputBaseCalculator {

    public TetsimOutputPerfectshiftCalculator(TetsimDataset dataset, Integer percentageChange) {
        super(dataset, percentageChange);
    }

    @Override
    protected String getShifting() {
        return "Perfectshift";
    }

    @Override
    public BigDecimal calculateAbsChangeShift(final TobaccoProduct tobaccoProduct) {
        return BigDecimal.ZERO;
    }
}
