package org.devgateway.toolkit.persistence.tetsim;

import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputOvershiftCalculator;
import org.junit.Before;
import org.junit.Test;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.ILLICIT;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.IMPORTED;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.POPULAR;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.PREMIUM;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TetsimOutputOvershiftCalculatorNumbersTest extends TetsimOutputBaseCalculatorTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testPopularNetTaxPrice() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(16.75, tetsimOutputCalculator.calculateNetTaxPrice(POPULAR)
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Overshift Net of Tax for Popular Product");
    }

    @Test
    public void testIllicitNetTaxPrice() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(22.38, tetsimOutputCalculator.calculateNetTaxPrice(ILLICIT)
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Overshift Net of Tax for Illicit Product");
    }

    @Test
    public void testPopularExciseTaxOnDomesticProduction() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(22.54, tetsimOutputCalculator.calculateExciseTaxDomesticProduction(POPULAR)
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Overshift Excise Tax on domestic production for Popular Product");
    }

    @Test
    public void testPremiumVat() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(6.99, tetsimOutputCalculator.calculateVat(PREMIUM)
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Overshift VAT for Premium Product");
    }

    @Test
    public void testImportedRetailPrice() {
        TetsimOutputOvershiftCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        assertEquals(50.62, tetsimOutputCalculator.calculateRetailPrice(IMPORTED)
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Overshift Retail Price for Imported Product");
    }

}