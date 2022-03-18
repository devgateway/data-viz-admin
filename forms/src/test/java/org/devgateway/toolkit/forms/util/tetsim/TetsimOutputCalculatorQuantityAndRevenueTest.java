package org.devgateway.toolkit.forms.util.tetsim;

import org.junit.Before;
import org.junit.Test;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TetsimOutputCalculatorQuantityAndRevenueTest extends TetsimOutputBaseCalculatorTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testBaselineTotalConsumption() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(754.4, tetsimOutputCalculator.calculateBaselineTotalLegalConsumption()
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check baseline Total Consumption");
    }

    @Test
    public void testPreConsumption() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(54.9, tetsimOutputCalculator.calculatePreConsumption("Imported")
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Pre Consumption for Imported Tobacco Product");
    }

    @Test
    public void testPreConsumptionIllicit() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(369.9, tetsimOutputCalculator.calculatePreConsumption("Illicit")
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Pre Consumption for Illicit Tobacco Product");
    }

    @Test
    public void testGainFromPrice() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(17.8, tetsimOutputCalculator.calculateGainFromPrice("Illicit")
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Gain From Price for Illicit Tobacco Product");
    }

    @Test
    public void testConsumptionPopular() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(432.2, tetsimOutputCalculator.calculateConsumption("Popular")
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Consumption for Popular Tobacco Product");
    }

    @Test
    public void testConsumptionIllicit() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(387.6, tetsimOutputCalculator.calculateConsumption("Illicit")
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Consumption for Illicit Tobacco Product");
    }

}