package org.devgateway.toolkit.persistence.util.tetsim;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dao.data.TobaccoProduct;

import java.math.BigDecimal;

import static org.devgateway.toolkit.persistence.util.tetsim.TetsimUtil.getTobaccoProductValueFromVariable;

/**
 * @author vchihai
 */
public class TetsimOutputUndershiftCalculator extends TetsimOutputBaseCalculator {

    public TetsimOutputUndershiftCalculator(TetsimDataset dataset, Integer percentageChange) {
        super(dataset, percentageChange);
    }

    @Override
    protected String getShifting() {
        return "Undershift";
    }

    @Override
    public BigDecimal calculateAbsChangeShift(final TobaccoProduct tobaccoProduct) {
        BigDecimal exciseTax = getTobaccoProductValueFromVariable(dataset.getExciseTax(), tobaccoProduct);
        BigDecimal undershifting = getTobaccoProductValueFromVariable(dataset.getUndershifting(), tobaccoProduct);

        return exciseTax.multiply(BigDecimal.valueOf(percentageChange))
                .divide(HUNDRED)
                .multiply(undershifting);
    }

}
