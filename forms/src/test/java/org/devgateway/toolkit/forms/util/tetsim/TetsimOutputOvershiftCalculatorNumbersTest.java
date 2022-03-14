package org.devgateway.toolkit.forms.util.tetsim;

import org.junit.Before;
import org.junit.Test;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TetsimOutputOvershiftCalculatorNumbersTest extends TetsimOutputBaseCalculatorTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testPopularNetTaxPrice() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        assertEquals(16.75, tetsimOutputCalculator.calculateNetTaxPrice("Popular")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Overshift Net of Tax for Popular Product");
    }

    @Test
    public void testIllicitNetTaxPrice() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        assertEquals(22.38, tetsimOutputCalculator.calculateNetTaxPrice("Illicit")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Overshift Net of Tax for Illicit Product");
    }

    @Test
    public void testPopularExciseTaxOnDomesticProduction() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        assertEquals(22.54, tetsimOutputCalculator.calculateExciseTaxDomesticProduction("Popular")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Overshift Excise Tax on domestic production for Popular Product");
    }

    @Test
    public void testPremiumVat() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        assertEquals(6.99, tetsimOutputCalculator.calculateVat("Premium")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Overshift VAT for Premium Product");
    }

    @Test
    public void testImportedRetailPrice() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        assertEquals(50.62, tetsimOutputCalculator.calculateRetailPrice("Imported")
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Overshift Retail Price for Imported Product");
    }

}