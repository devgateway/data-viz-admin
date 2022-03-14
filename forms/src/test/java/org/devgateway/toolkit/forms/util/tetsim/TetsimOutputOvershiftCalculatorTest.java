package org.devgateway.toolkit.forms.util.tetsim;

import org.devgateway.toolkit.persistence.dto.TetsimOutput;
import org.junit.Before;
import org.junit.Test;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TetsimOutputOvershiftCalculatorTest extends TetsimOutputBaseCalculatorTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testConsumptionLegalNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(754.4, output.getConsumptionLegal()
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Consumption Legal TETSIM Output Overshift");
    }

    @Test
    public void testConsumptionLegalWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(680.2, output.getConsumptionLegal()
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Consumption Legal TETSIM Output Overshift");
    }

    @Test
    public void testConsumptionIllicitlNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(395.6, output.getConsumptionIllicit()
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Consumption Illicit TETSIM Output Overshift");
    }

    @Test
    public void testConsumptionIllicitWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(387.6, output.getConsumptionIllicit()
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Consumption Illicit TETSIM Output Overshift");
    }

    @Test
    public void testExciseBurdenNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(47.0, output.getExciseBurden()
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Excise Burden TETSIM Output Overshift");
    }

    @Test
    public void testExciseBurdenWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(49.9, output.getExciseBurden()
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Excise Burden TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testTotalTaxBurdenNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(60.0, output.getTotalTaxBurden()
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Total Tax Burden TETSIM Output Overshift");
    }

    @Test
    public void testTotalTaxBurdenWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(62.9, output.getTotalTaxBurden()
                .setScale(1, ROUND_HALF_UP).doubleValue(), "Check Total Tax Burden TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testRetailPriceNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(40.0, output.getRetailPrice()
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Retail Price TETSIM Output Overshift (Baseline)");
    }

    @Test
    public void testRetailPriceWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(45.18, output.getRetailPrice()
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Retail Price TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testNOTNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(16.0, output.getNot()
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check NOT TETSIM Output Overshift");
    }

    @Test
    public void testNOTWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(16.75, output.getNot()
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check NOT TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testExciseTaxNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(18.78, output.getExciseTax()
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Excise Tax TETSIM Output Overshift");
    }

    @Test
    public void testExciseTaxWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(22.54, output.getExciseTax()
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Excise Tax TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testVatNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(5.22, output.getVat()
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check VAT TETSIM Output Overshift");
    }

    @Test
    public void testVatWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(5.89, output.getVat()
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check VAT TETSIM Output Overshift (Tax Change)");
    }

    @Test
    public void testLevyNoTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 0.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(0.00, output.getLevy()
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Levy TETSIM Output Overshift");
    }

    @Test
    public void testLevyWithTaxChange() {
        TetsimOutputCalculator tetsimOutputCalculator = new TetsimOutputOvershiftCalculator(datasetWithAllTobaccoProducts, 20.0);
        TetsimOutput output = tetsimOutputCalculator.calculate();
        assertEquals(0.00, output.getLevy()
                .setScale(2, ROUND_HALF_UP).doubleValue(), "Check Levy TETSIM Output Overshift (Tax Change)");
    }

}