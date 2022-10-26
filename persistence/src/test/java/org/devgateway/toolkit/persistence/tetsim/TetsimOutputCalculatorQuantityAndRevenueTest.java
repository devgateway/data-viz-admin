package org.devgateway.toolkit.persistence.tetsim;

import org.devgateway.toolkit.persistence.dao.data.TobaccoProduct;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputOvershiftCalculator;
import org.junit.Before;
import org.junit.Test;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.ILLICIT;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.IMPORTED;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.POPULAR;
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
        assertEquals(54.9, tetsimOutputCalculator.calculatePreConsumption(IMPORTED)
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Pre Consumption for Imported Tobacco Product");
    }

    @Test
    public void testPreConsumptionIllicit() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(369.9, tetsimOutputCalculator.calculatePreConsumption(ILLICIT)
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Pre Consumption for Illicit Tobacco Product");
    }

    @Test
    public void testGainFromPrice() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(17.8, tetsimOutputCalculator.calculateGainFromPrice(ILLICIT)
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Gain From Price for Illicit Tobacco Product");
    }

    @Test
    public void testConsumptionPopular() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(432.2, tetsimOutputCalculator.calculateConsumption(POPULAR)
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Consumption for Popular Tobacco Product");
    }

    @Test
    public void testConsumptionIllicit() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(387.6, tetsimOutputCalculator.calculateConsumption(ILLICIT)
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Consumption for Illicit Tobacco Product");
    }

}