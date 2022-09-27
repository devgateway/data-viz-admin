package org.devgateway.toolkit.persistence.tetsim;

import org.devgateway.toolkit.persistence.dto.TetsimOutput;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputCalculator;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputOvershiftCalculator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TetsimOutputOvershiftCalculatorTest extends TetsimOutputBaseCalculatorTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testConsumptionLegalNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(754.4, output.getLegalConsumption(), delta, "Check Consumption Legal TETSIM Output Overshift");
    }

    @Test
    public void testConsumptionLegalWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(680.19, output.getLegalConsumption(), delta, "Check Consumption Legal TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testConsumptionIllicitNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(395.6, output.getConsumptionIllicit(), delta, "Check Consumption Illicit TETSIM Output Overshift");
    }

    @Test
    public void testConsumptionIllicitWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(387.62, output.getConsumptionIllicit(), delta, "Check Consumption Illicit TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testTotalExciseRevenueNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(14167.63, output.getExciseRev(), delta, "Check Total Legal Excise Revenue TETSIM Output Overshift");
    }

    @Test
    public void testTotalExciseRevenueWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(15328.9, output.getExciseRev(), delta, "Check Total Legal Excise Revenue TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testTotalLegalGovRevenueNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(18051.37, output.getTotalGovRev(), delta, "Check Total Legal Government Revenue TETSIM Output Overshift");
    }

    @Test
    public void testTotalLegalGovRevenueWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(19315.54, output.getTotalGovRev(), delta, "Check Total Legal Government Revenue TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testExciseBurdenNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(46.95, output.getExciseBurden(), delta, "Check Excise Burden TETSIM Output Overshift");
    }

    @Test
    public void testExciseBurdenWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(49.87, output.getExciseBurden(), delta, "Check Excise Burden TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testTotalTaxBurdenNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(60.0, output.getTotalTaxBurden(), delta, "Check Total Tax Burden TETSIM Output Overshift");
    }

    @Test
    public void testTotalTaxBurdenWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(62.92, output.getTotalTaxBurden(), delta, "Check Total Tax Burden TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testRetailPriceNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(40.0, output.getRetailPrice(), delta, "Check Retail Price TETSIM Output Overshift (Baseline)");
    }

    @Test
    public void testRetailPriceWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(45.18, output.getRetailPrice(), delta, "Check Retail Price TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testNOTNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(16.0, output.getNot(), delta, "Check NOT TETSIM Output Overshift");
    }

    @Test
    public void testNOTWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(16.75, output.getNot(), delta, "Check NOT TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testExciseTaxNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(18.78, output.getExciseTax(), delta, "Check Excise Tax TETSIM Output Overshift");
    }

    @Test
    public void testExciseTaxWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(22.54, output.getExciseTax(), delta, "Check Excise Tax TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testVatNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(5.22, output.getVat(), delta, "Check VAT TETSIM Output Overshift");
    }

    @Test
    public void testVatWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(5.89, output.getVat(), delta, "Check VAT TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testLevyNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(0.00, output.getLevy(), delta, "Check Levy TETSIM Output Overshift");
    }

    @Test
    public void testLevyWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20);
        TetsimOutput output = tetsimOutputCalculator.calculate("Popular");
        assertEquals(0.00, output.getLevy(), delta, "Check Levy TETSIM Output Overshift (Tax Change)");
    }

}