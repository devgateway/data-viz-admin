package org.devgateway.toolkit.forms.util.tetsim;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TetsimOutputCalculatorAbsChangeTest extends TetsimOutputBaseCalculatorTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testOvershiftAbsChangeBaseline() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift("Imported").doubleValue(),
                delta, "Check Abs change overshift imported baseline");
        assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift("Premium").doubleValue(),
                delta, "Check Abs change overshift imported baseline");
    }

    @Test
    public void testOvershiftAbsChange() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertAll("TETSIM Overshift Abs Change",
                () -> assertEquals(0.75, tetsimOutputCalculator.calculateAbsChangeShift("Popular").doubleValue(),
                        delta, "Check Abs change overshift popular"),
                () -> assertEquals(0.38, tetsimOutputCalculator.calculateAbsChangeShift("Discount").doubleValue(),
                        delta, "Check Abs change overshift discount"),
                () -> assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift("Illicit").doubleValue(),
                        delta, "Check Abs change overshift illicit")
        );
    }

    @Test
    public void testUndershiftAbsChangeBaseline() {
        TetsimOutputUndershiftCalculator tetsimOutputCalculator = new TetsimOutputUndershiftCalculator(datasetWithAllTobaccoProducts, 0);
        assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift("Imported").doubleValue(),
                delta, "Check Abs change undershift imported baseline");
        assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift("Premium").doubleValue(),
                delta, "Check Abs change undershift premium baseline");
    }

    @Test
    public void testUndershiftAbsChange() {
        TetsimOutputUndershiftCalculator tetsimOutputCalculator = new TetsimOutputUndershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertAll("TETSIM Undershift Abs Change",
                () -> assertEquals(-1.13, tetsimOutputCalculator.calculateAbsChangeShift("Popular").doubleValue(),
                        delta, "Check Abs change undershift popular"),
                () -> assertEquals(-1.88, tetsimOutputCalculator.calculateAbsChangeShift("Discount").doubleValue(),
                        delta, "Check Abs change undershift discount"),
                () -> assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift("Illicit").doubleValue(),
                        delta, "Check Abs change undershift illicit")
        );
    }

}