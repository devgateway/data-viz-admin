package org.devgateway.toolkit.forms.util.tetsim;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;

import java.math.BigDecimal;

import static org.devgateway.toolkit.forms.util.tetsim.TetsimUtil.getTobaccoProductValueFromVariable;

/**
 * @author vchihai
 */
public class TetsimOutputUndershiftCalculator extends TetsimOutputBaseCalculator {

    public TetsimOutputUndershiftCalculator(TetsimDataset dataset, Integer percentageChange) {
        super(dataset, percentageChange);
    }

    @Override
    protected BigDecimal calculateAbsChangeShift(final String tobaccoProduct) {
        BigDecimal exciseTax = getTobaccoProductValueFromVariable(dataset.getExciseTax(), tobaccoProduct);
        BigDecimal undershifting = getTobaccoProductValueFromVariable(dataset.getUndershifting(), tobaccoProduct);

        return exciseTax.multiply(BigDecimal.valueOf(percentageChange))
                .divide(HUNDRED)
                .multiply(undershifting);
    }
}
