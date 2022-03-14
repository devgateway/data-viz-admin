package org.devgateway.toolkit.forms.util.tetsim;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;

import java.math.BigDecimal;

import static org.devgateway.toolkit.forms.util.tetsim.TetsimUtil.getTobaccoProductValueFromVariable;

/**
 * @author vchihai
 */
public class TetsimOutputOvershiftCalculator extends TetsimOutputBaseCalculator {

    public TetsimOutputOvershiftCalculator(TetsimDataset dataset, Double percentageChange) {
        super(dataset, percentageChange);
    }

    @Override
    protected BigDecimal calculateAbsChangeShift(final String tobaccoProduct) {
        BigDecimal exciseTax = getTobaccoProductValueFromVariable(dataset.getExciseTax(), tobaccoProduct);
        BigDecimal overshifting = getTobaccoProductValueFromVariable(dataset.getOvershifting(), tobaccoProduct);

        return exciseTax.multiply(BigDecimal.valueOf(percentageChange))
                .divide(HUNDRED)
                .multiply(overshifting);
    }
}
