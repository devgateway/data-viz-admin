package org.devgateway.toolkit.forms.util.tetsim;

import org.devgateway.toolkit.persistence.dto.TetsimOutput;
import org.junit.Before;
import org.junit.Test;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TetsimOutputOvershiftCalculatorAbsChangeTest extends TetsimOutputBaseCalculatorTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testAbsChangeImportedBaseline() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift("Imported")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Abs change overshift imported baseline");
    }

    @Test
    public void testAbsChangePopular() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(0.75, tetsimOutputCalculator.calculateAbsChangeShift("Popular")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Abs change overshift popular");
    }

    @Test
    public void testAbsChangeDiscount() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(0.38, tetsimOutputCalculator.calculateAbsChangeShift("Discount")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Abs change overshift discount");
    }

    @Test
    public void testAbsChangeIllicit() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift("Illicit")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Abs change overshift illicit");
    }

}