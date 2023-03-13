package org.devgateway.toolkit.persistence.tetsim;

import org.devgateway.toolkit.persistence.dao.data.TobaccoProduct;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputOvershiftCalculator;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputPerfectshiftCalculator;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputUndershiftCalculator;
import org.junit.Before;
import org.junit.Test;

import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.DISCOUNT;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.ILLICIT;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.IMPORTED;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.POPULAR;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.PREMIUM;
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
        assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift(IMPORTED).doubleValue(),
                delta, "Check Abs change overshift imported baseline");
        assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift(PREMIUM).doubleValue(),
                delta, "Check Abs change overshift premium baseline");
    }

    @Test
    public void testOvershiftAbsChange() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertAll("TETSIM Overshift Abs Change",
                () -> assertEquals(0.75, tetsimOutputCalculator.calculateAbsChangeShift(POPULAR).doubleValue(),
                        delta, "Check Abs change overshift popular"),
                () -> assertEquals(0.38, tetsimOutputCalculator.calculateAbsChangeShift(DISCOUNT).doubleValue(),
                        delta, "Check Abs change overshift discount"),
                () -> assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift(ILLICIT).doubleValue(),
                        delta, "Check Abs change overshift illicit")
        );
    }

    @Test
    public void testUndershiftAbsChangeBaseline() {
        TetsimOutputUndershiftCalculator tetsimOutputCalculator = new TetsimOutputUndershiftCalculator(datasetWithAllTobaccoProducts, 0);
        assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift(IMPORTED).doubleValue(),
                delta, "Check Abs change undershift imported baseline");
        assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift(PREMIUM).doubleValue(),
                delta, "Check Abs change undershift premium baseline");
    }

    @Test
    public void testUndershiftAbsChange() {
        TetsimOutputUndershiftCalculator tetsimOutputCalculator = new TetsimOutputUndershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertAll("TETSIM Undershift Abs Change",
                () -> assertEquals(-1.13, tetsimOutputCalculator.calculateAbsChangeShift(POPULAR).doubleValue(),
                        delta, "Check Abs change undershift popular"),
                () -> assertEquals(-0.56, tetsimOutputCalculator.calculateAbsChangeShift(DISCOUNT).doubleValue(),
                        delta, "Check Abs change undershift discount"),
                () -> assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift(ILLICIT).doubleValue(),
                        delta, "Check Abs change undershift illicit")
        );
    }

    @Test
    public void testPerfectshiftAbsChange() {
        TetsimOutputPerfectshiftCalculator tetsimOutputCalculator = new TetsimOutputPerfectshiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertAll("TETSIM Perfectshift Abs Change",
                () -> assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift(POPULAR).doubleValue(),
                        delta, "Check Abs change undershift popular"),
                () -> assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift(DISCOUNT).doubleValue(),
                        delta, "Check Abs change undershift discount"),
                () -> assertEquals(0.00, tetsimOutputCalculator.calculateAbsChangeShift(ILLICIT).doubleValue(),
                        delta, "Check Abs change undershift illicit")
        );
    }

}