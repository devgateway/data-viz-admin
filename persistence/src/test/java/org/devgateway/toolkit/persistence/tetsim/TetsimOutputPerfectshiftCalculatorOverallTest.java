package org.devgateway.toolkit.persistence.tetsim;

import org.devgateway.toolkit.persistence.dto.TetsimOutput;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputPerfectshiftCalculator;
import org.junit.Before;
import org.junit.Test;

import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.POPULAR;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TetsimOutputPerfectshiftCalculatorOverallTest extends TetsimOutputBaseCalculatorTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testOverallOutputBaseline() {
        TetsimOutputPerfectshiftCalculator tetsimOutputCalculator = new TetsimOutputPerfectshiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate(POPULAR);

        assertAll("TETSIM Perfectshift Baseline",
                () -> assertEquals(754.40, output.getLegalConsumption(), delta, "TETSIM Perfectshift - Consumption Legal"),
                () -> assertEquals(0, output.getLegalConsumptionChange(), delta, "TETSIM Perfectshift - Consumption Legal Change"),
                () -> assertEquals(395.60, output.getConsumptionIllicit(), delta, "TETSIM Perfectshift - Consumption Illicit"),
                () -> assertEquals(14167.63, output.getExciseRev(), delta, "TETSIM Perfectshift - Excise Revenue"),
                () -> assertEquals(0, output.getExciseRevChange(), delta, "TETSIM Perfectshift - Excise Revenue Change"),
                () -> assertEquals(18051.37, output.getTotalGovRev(), delta, "TETSIM Perfectshift - Government Revenue"),
                () -> assertEquals(46.95, output.getExciseBurden(), delta, "TETSIM Perfectshift - Excise Burden"),
                () -> assertEquals(60.00, output.getTotalTaxBurden(), delta, "TETSIM Perfectshift - Total Tax Burden"),
                () -> assertEquals(40.00, output.getRetailPrice(), delta, "TETSIM Perfectshift - Retail Price"),
                () -> assertEquals(16.00, output.getNot(), delta, "TETSIM Perfectshift - NOT"),
                () -> assertEquals(18.78, output.getExciseTax(), delta, "TETSIM Perfectshift - Excise Tax"),
                () -> assertEquals(5.21, output.getVat(), delta, "TETSIM Perfectshift - VAT"),
                () -> assertEquals(0.00, output.getLevy(), delta, "TETSIM Perfectshift - Levy"));
    }

    @Test
    public void testOverallOutputWithTaxChange() {
        TetsimOutputPerfectshiftCalculator tetsimOutputCalculator = new TetsimOutputPerfectshiftCalculator(datasetWithAllTobaccoProducts, 30);
        TetsimOutput output = tetsimOutputCalculator.calculate(POPULAR);

        assertAll("TETSIM Perfectshift With 30% Tax Change",
                () -> assertEquals(661.37, output.getLegalConsumption(), delta, "TETSIM Perfectshift - Consumption Legal"),
                () -> assertEquals(-12.33, output.getLegalConsumptionChange(), delta, "TETSIM Perfectshift - Consumption Legal Change"),
                () -> assertEquals(384.78, output.getConsumptionIllicit(), delta, "TETSIM Perfectshift - Consumption Illicit"),
                () -> assertEquals(16146.9, output.getExciseRev(), delta, "TETSIM Perfectshift - Excise Revenue"),
                () -> assertEquals(13.97, output.getExciseRevChange(), delta, "TETSIM Perfectshift - Excise Revenue Change"),
                () -> assertEquals(20146.86, output.getTotalGovRev(), delta, "TETSIM Perfectshift - Government Revenue"),
                () -> assertEquals(52.53, output.getExciseBurden(), delta, "TETSIM Perfectshift - Excise Burden"),
                () -> assertEquals(65.57, output.getTotalTaxBurden(), delta, "TETSIM Perfectshift - Total Tax Burden"),
                () -> assertEquals(46.48, output.getRetailPrice(), delta, "TETSIM Perfectshift - Retail Price"),
                () -> assertEquals(16.00, output.getNot(), delta, "TETSIM Perfectshift - NOT"),
                () -> assertEquals(24.41, output.getExciseTax(), delta, "TETSIM Perfectshift - Excise Tax"),
                () -> assertEquals(6.06, output.getVat(), delta, "TETSIM Perfectshift - VAT"),
                () -> assertEquals(0.00, output.getLevy(), delta, "TETSIM Perfectshift - Levy"));
    }

}