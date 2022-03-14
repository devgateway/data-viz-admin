package org.devgateway.toolkit.forms.util.tetsim;

import org.junit.Before;
import org.junit.Test;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TetsimOutputCalculatorBaselineNumbersTest extends TetsimOutputBaseCalculatorTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testBaselineNetOfTaxPrice() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        assertEquals(19.43, tetsimOutputCalculator.calculateBaselineNetTaxPrice("Imported")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check baseline Net of Tax for Imported Product");
    }

    @Test
    public void testBaselineNetOfTaxPriceWithPercentageChange() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        assertEquals(16.00, tetsimOutputCalculator.calculateBaselineNetTaxPrice("Popular")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check baseline Net of Tax for Popular Product with percentage change");
    }

    @Test
    public void testBaselineIllicitNetOfTaxPriceWithPercentageChange() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        assertEquals(20.00, tetsimOutputCalculator.calculateBaselineNetTaxPrice("Illicit")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check baseline Net of Tax Price for Illicit Product with percentage change");
    }

    @Test
    public void testBaselineIllicitRetailPrice() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        assertEquals(20.00, tetsimOutputCalculator.calculateBaselineRetailPrice("Illicit")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check baseline Retail Price for Illicit Product");
    }

    @Test
    public void testBaselineRetailPriceWithPercentageChange() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        assertEquals(40.00, tetsimOutputCalculator.calculateBaselineRetailPrice("Popular")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check baseline Retail Price for Popular Product with percentage change");
    }

    @Test
    public void testBaselineIllicitExciseTaxOnDomestic() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        assertEquals(0.0, tetsimOutputCalculator.calculateBaselineExciseTaxOnDomesticProduction("Illicit")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check baseline Excise Tax for Illicit Product");
    }

    @Test
    public void testBaselineImportedExciseTaxOnDomestic() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        assertEquals(0.0, tetsimOutputCalculator.calculateBaselineExciseTaxOnDomesticProduction("Imported")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check baseline Excise Tax for Imported Product");
    }

    @Test
    public void testBaselinePopularExciseTaxOnDomestic() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        assertEquals(18.78, tetsimOutputCalculator.calculateBaselineExciseTaxOnDomesticProduction("Popular")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check baseline Excise Tax for Popular Product");
    }

    @Test
    public void testBaselinePopularVAT() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        assertEquals(5.22, tetsimOutputCalculator.calculateBaselineVAT("Popular")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check baseline VAT for Popular Product");
    }

    @Test
    public void testBaselineDiscountExciseBurden() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        assertEquals(67.07, tetsimOutputCalculator.calculateBaselineExciseBurden("Discount")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check baseline Excise Burden for Discount Product");
    }

    @Test
    public void testBaselinePremiumTotalTaxBurden() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        assertEquals(52.17, tetsimOutputCalculator.calculateBaselineTotalTaxBurden("Premium")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check baseline Total Tax Burden for Premium Product");
    }

}