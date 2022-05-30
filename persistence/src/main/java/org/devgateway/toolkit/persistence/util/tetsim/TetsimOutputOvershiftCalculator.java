package org.devgateway.toolkit.persistence.util.tetsim;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;

import java.math.BigDecimal;

import static org.devgateway.toolkit.persistence.util.tetsim.TetsimUtil.getTobaccoProductValueFromVariable;

/**
 * @author vchihai
 */
public class TetsimOutputOvershiftCalculator extends TetsimOutputBaseCalculator {

    public TetsimOutputOvershiftCalculator(TetsimDataset dataset, Integer percentageChange) {
        super(dataset, percentageChange);
    }

    @Override
    protected String getShifting() {
        return "Overshift";
    }

    @Override
    public BigDecimal calculateAbsChangeShift(final String tobaccoProduct) {
        BigDecimal exciseTax = getTobaccoProductValueFromVariable(dataset.getExciseTax(), tobaccoProduct);
        BigDecimal overshifting = getTobaccoProductValueFromVariable(dataset.getOvershifting(), tobaccoProduct);

        return exciseTax.multiply(BigDecimal.valueOf(percentageChange))
                .divide(HUNDRED)
                .multiply(overshifting);
    }
}
