package org.devgateway.toolkit.forms.util.tetsim;

import org.devgateway.toolkit.persistence.dto.TetsimOutput;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TetsimOutputUndershiftCalculatorOverallTest extends TetsimOutputBaseCalculatorTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testOverallOutputBaseline() {
        TetsimOutputUndershiftCalculator tetsimOutputCalculator = new TetsimOutputUndershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate();

        assertAll("TETSIM Undershift Baseline",
                () -> assertEquals(754.40, output.getConsumptionLegal(), delta, "TETSIM Undershift - Consumption Legal"),
                () -> assertEquals(395.60, output.getConsumptionIllicit(), delta, "TETSIM Undershift - Consumption Illicit"),
                () -> assertEquals(14167.63, output.getExciseRev(), delta, "TETSIM Undershift - Excise Revenue"),
                () -> assertEquals(18051.37, output.getTotalGovRev(), delta, "TETSIM Undershift - Government Revenue"),
                () -> assertEquals(46.95, output.getExciseBurden(), delta, "TETSIM Undershift - Excise Burden"),
                () -> assertEquals(60.00, output.getTotalTaxBurden(), delta, "TETSIM Undershift - Total Tax Burden"),
                () -> assertEquals(40.00, output.getRetailPrice(), delta, "TETSIM Undershift - Retail Price"),
                () -> assertEquals(16.00, output.getNot(), delta, "TETSIM Undershift - NOT"),
                () -> assertEquals(18.78, output.getExciseTax(), delta, "TETSIM Undershift - Excise Tax"),
                () -> assertEquals(5.21, output.getVat(), delta, "TETSIM Undershift - VAT"),
                () -> assertEquals(0.00, output.getLevy(), delta, "TETSIM Undershift - Levy"));
    }

    @Test
    public void testOverallOutputWithTaxChange() {
        TetsimOutputUndershiftCalculator tetsimOutputCalculator = new TetsimOutputUndershiftCalculator(datasetWithAllTobaccoProducts, 30);
        TetsimOutput output = tetsimOutputCalculator.calculate();

        assertAll("TETSIM Undershift With 30% Tax Change",
                () -> assertEquals(694.62, output.getConsumptionLegal(), delta, "TETSIM Undershift - Consumption Legal"),
                () -> assertEquals(390.14, output.getConsumptionIllicit(), delta, "TETSIM Undershift - Consumption Illicit"),
                () -> assertEquals(16958.46, output.getExciseRev(), delta, "TETSIM Undershift - Excise Revenue"),
                () -> assertEquals(20945.90, output.getTotalGovRev(), delta, "TETSIM Undershift - Government Revenue"),
                () -> assertEquals(54.82, output.getExciseBurden(), delta, "TETSIM Undershift - Excise Burden"),
                () -> assertEquals(67.86, output.getTotalTaxBurden(), delta, "TETSIM Undershift - Total Tax Burden"),
                () -> assertEquals(44.54, output.getRetailPrice(), delta, "TETSIM Undershift - Retail Price"),
                () -> assertEquals(14.31, output.getNot(), delta, "TETSIM Undershift - NOT"),
                () -> assertEquals(24.41, output.getExciseTax(), delta, "TETSIM Undershift - Excise Tax"),
                () -> assertEquals(5.81, output.getVat(), delta, "TETSIM Undershift - VAT"),
                () -> assertEquals(0.00, output.getLevy(), delta, "TETSIM Undershift - Levy"));
    }

}