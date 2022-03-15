package org.devgateway.toolkit.forms.util.tetsim;

import org.devgateway.toolkit.persistence.dto.TetsimOutput;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TetsimOutputOvershiftCalculatorOverallTest extends TetsimOutputBaseCalculatorTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testOverallOutputBaseline() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate();

        assertAll("TETSIM Overshift Baseline",
                () -> assertEquals(754.40, output.getConsumptionLegal(), delta, "TETSIM Overshift - Consumption Legal"),
                () -> assertEquals(395.60, output.getConsumptionIllicit(), delta, "TETSIM Overshift - Consumption Illicit"),
                () -> assertEquals(14167.63, output.getExciseRev(), delta, "TETSIM Overshift - Excise Revenue"),
                () -> assertEquals(18051.37, output.getTotalGovRev(), delta, "TETSIM Overshift - Government Revenue"),
                () -> assertEquals(46.95, output.getExciseBurden(), delta, "TETSIM Overshift - Excise Burden"),
                () -> assertEquals(60.00, output.getTotalTaxBurden(), delta, "TETSIM Overshift - Total Tax Burden"),
                () -> assertEquals(40.00, output.getRetailPrice(), delta, "TETSIM Overshift - Retail Price"),
                () -> assertEquals(16.00, output.getNot(), delta, "TETSIM Overshift - NOT"),
                () -> assertEquals(18.78, output.getExciseTax(), delta, "TETSIM Overshift - Excise Tax"),
                () -> assertEquals(5.21, output.getVat(), delta, "TETSIM Overshift - VAT"),
                () -> assertEquals(0.00, output.getLevy(), delta, "TETSIM Overshift - Levy"));
    }

    @Test
    public void testOverallOutputWithTaxChange() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 30);
        TetsimOutput output = tetsimOutputCalculator.calculate();

        assertAll("TETSIM Overshift With 30% Tax Change",
                () -> assertEquals(648.79, output.getConsumptionLegal(), delta, "TETSIM Overshift - Consumption Legal"),
                () -> assertEquals(383.73, output.getConsumptionIllicit(), delta, "TETSIM Overshift - Consumption Illicit"),
                () -> assertEquals(15839.61, output.getExciseRev(), delta, "TETSIM Overshift - Excise Revenue"),
                () -> assertEquals(19873.25, output.getTotalGovRev(), delta, "TETSIM Overshift - Government Revenue"),
                () -> assertEquals(51.10, output.getExciseBurden(), delta, "TETSIM Overshift - Excise Burden"),
                () -> assertEquals(64.15, output.getTotalTaxBurden(), delta, "TETSIM Overshift - Total Tax Burden"),
                () -> assertEquals(47.77, output.getRetailPrice(), delta, "TETSIM Overshift - Retail Price"),
                () -> assertEquals(17.13, output.getNot(), delta, "TETSIM Overshift - NOT"),
                () -> assertEquals(24.41, output.getExciseTax(), delta, "TETSIM Overshift - Excise Tax"),
                () -> assertEquals(6.23, output.getVat(), delta, "TETSIM Overshift - VAT"),
                () -> assertEquals(0.00, output.getLevy(), delta, "TETSIM Overshift - Levy"));
    }

}