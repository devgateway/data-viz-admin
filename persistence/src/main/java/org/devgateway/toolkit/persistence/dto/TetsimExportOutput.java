package org.devgateway.toolkit.persistence.dto;

import org.devgateway.toolkit.persistence.dao.data.TobaccoProduct;

import java.io.Serializable;

/**
 *
 *
 * @author vchihai
 */
public class TetsimExportOutput implements Serializable {

    private Integer year;

    private Integer taxChange;

    private TobaccoProduct tobaccoProduct;

    private Double legalConsumptionOvershift;

    private Double legalConsumptionChangeOvershift;

    private Double consumptionIllicitOvershift;

    private Double exciseRevOvershift;

    private Double exciseRevChangeOvershift;

    private Double totalGovRevOvershift;

    private Double exciseBurdenOvershift;

    private Double totalTaxBurdenOvershift;

    private Double baselineTotalTaxBurdenOvershift;

    private Double retailPriceOvershift;

    private Double notOvershift;

    private Double exciseTaxOvershift;

    private Double vatOvershift;

    private Double levyOvershift;

    private Double legalConsumptionUndershift;

    private Double legalConsumptionChangeUndershift;

    private Double consumptionIllicitUndershift;

    private Double exciseRevUndershift;

    private Double exciseRevChangeUndershift;

    private Double totalGovRevUndershift;

    private Double exciseBurdenUndershift;

    private Double totalTaxBurdenUndershift;

    private Double baselineTotalTaxBurdenUndershift;

    private Double retailPriceUndershift;

    private Double notUndershift;

    private Double exciseTaxUndershift;

    private Double vatUndershift;

    private Double levyUndershift;

    private Double legalConsumptionPerfectshift;

    private Double legalConsumptionChangePerfectshift;

    private Double consumptionIllicitPerfectshift;

    private Double exciseRevPerfectshift;

    private Double exciseRevChangePerfectshift;

    private Double totalGovRevPerfectshift;

    private Double exciseBurdenPerfectshift;

    private Double totalTaxBurdenPerfectshift;

    private Double baselineTotalTaxBurdenPerfectshift;

    private Double retailPricePerfectshift;

    private Double notPerfectshift;

    private Double exciseTaxPerfectshift;

    private Double vatPerfectshift;

    private Double levyPerfectshift;

    public TetsimExportOutput() {
    }

    public TetsimExportOutput(TetsimOutput overShift, TetsimOutput underShift, TetsimOutput perfectShift) {
        this.year = overShift.getYear();
        this.taxChange = overShift.getTaxChange();
        this.tobaccoProduct = overShift.getTobaccoProduct();
        this.legalConsumptionOvershift = overShift.getLegalConsumption();
        this.legalConsumptionChangeOvershift = overShift.getLegalConsumptionChange();
        this.consumptionIllicitOvershift = overShift.getConsumptionIllicit();
        this.exciseRevOvershift = overShift.getExciseRev();
        this.exciseRevChangeOvershift = overShift.getExciseRevChange();
        this.totalGovRevOvershift = overShift.getTotalGovRev();
        this.exciseBurdenOvershift = overShift.getExciseBurden();
        this.totalTaxBurdenOvershift = overShift.getTotalTaxBurden();
        this.baselineTotalTaxBurdenOvershift = overShift.getTotalTaxBurdenBaseline();
        this.retailPriceOvershift = overShift.getRetailPrice();
        this.notOvershift = overShift.getNot();
        this.exciseTaxOvershift = overShift.getExciseTax();
        this.vatOvershift = overShift.getVat();
        this.levyOvershift = overShift.getLevy();
        this.legalConsumptionUndershift = underShift.getLegalConsumption();
        this.legalConsumptionChangeUndershift = underShift.getLegalConsumptionChange();
        this.consumptionIllicitUndershift = underShift.getConsumptionIllicit();
        this.exciseRevUndershift = underShift.getExciseRev();
        this.exciseRevChangeUndershift = underShift.getExciseRevChange();
        this.totalGovRevUndershift = underShift.getTotalGovRev();
        this.exciseBurdenUndershift = underShift.getExciseBurden();
        this.totalTaxBurdenUndershift = underShift.getTotalTaxBurden();
        this.baselineTotalTaxBurdenUndershift = underShift.getTotalTaxBurdenBaseline();
        this.retailPriceUndershift = underShift.getRetailPrice();
        this.notUndershift = underShift.getNot();
        this.exciseTaxUndershift = underShift.getExciseTax();
        this.vatUndershift = underShift.getVat();
        this.levyUndershift = underShift.getLevy();
        this.legalConsumptionPerfectshift = perfectShift.getLegalConsumption();
        this.legalConsumptionChangePerfectshift = perfectShift.getLegalConsumptionChange();
        this.consumptionIllicitPerfectshift = perfectShift.getConsumptionIllicit();
        this.exciseRevPerfectshift = perfectShift.getExciseRev();
        this.exciseRevChangePerfectshift = perfectShift.getExciseRevChange();
        this.totalGovRevPerfectshift = perfectShift.getTotalGovRev();
        this.exciseBurdenPerfectshift = perfectShift.getExciseBurden();
        this.totalTaxBurdenPerfectshift = perfectShift.getTotalTaxBurden();
        this.baselineTotalTaxBurdenPerfectshift = perfectShift.getTotalTaxBurdenBaseline();
        this.retailPricePerfectshift = perfectShift.getRetailPrice();
        this.notPerfectshift = perfectShift.getNot();
        this.exciseTaxPerfectshift = perfectShift.getExciseTax();
        this.vatPerfectshift = perfectShift.getVat();
        this.levyPerfectshift = perfectShift.getLevy();
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    public Integer getTaxChange() {
        return taxChange;
    }

    public void setTaxChange(final Integer taxChange) {
        this.taxChange = taxChange;
    }

    public TobaccoProduct getTobaccoProduct() {
        return tobaccoProduct;
    }

    public void setTobaccoProduct(final TobaccoProduct tobaccoProduct) {
        this.tobaccoProduct = tobaccoProduct;
    }

    public Double getLegalConsumptionOvershift() {
        return legalConsumptionOvershift;
    }

    public void setLegalConsumptionOvershift(final Double legalConsumptionOvershift) {
        this.legalConsumptionOvershift = legalConsumptionOvershift;
    }

    public Double getLegalConsumptionChangeOvershift() {
        return legalConsumptionChangeOvershift;
    }

    public void setLegalConsumptionChangeOvershift(final Double legalConsumptionChangeOvershift) {
        this.legalConsumptionChangeOvershift = legalConsumptionChangeOvershift;
    }

    public Double getConsumptionIllicitOvershift() {
        return consumptionIllicitOvershift;
    }

    public void setConsumptionIllicitOvershift(final Double consumptionIllicitOvershift) {
        this.consumptionIllicitOvershift = consumptionIllicitOvershift;
    }

    public Double getExciseRevOvershift() {
        return exciseRevOvershift;
    }

    public void setExciseRevOvershift(final Double exciseRevOvershift) {
        this.exciseRevOvershift = exciseRevOvershift;
    }

    public Double getExciseRevChangeOvershift() {
        return exciseRevChangeOvershift;
    }

    public void setExciseRevChangeOvershift(final Double exciseRevChangeOvershift) {
        this.exciseRevChangeOvershift = exciseRevChangeOvershift;
    }

    public Double getTotalGovRevOvershift() {
        return totalGovRevOvershift;
    }

    public void setTotalGovRevOvershift(final Double totalGovRevOvershift) {
        this.totalGovRevOvershift = totalGovRevOvershift;
    }

    public Double getExciseBurdenOvershift() {
        return exciseBurdenOvershift;
    }

    public void setExciseBurdenOvershift(final Double exciseBurdenOvershift) {
        this.exciseBurdenOvershift = exciseBurdenOvershift;
    }

    public Double getTotalTaxBurdenOvershift() {
        return totalTaxBurdenOvershift;
    }

    public void setTotalTaxBurdenOvershift(final Double totalTaxBurdenOvershift) {
        this.totalTaxBurdenOvershift = totalTaxBurdenOvershift;
    }

    public Double getRetailPriceOvershift() {
        return retailPriceOvershift;
    }

    public void setRetailPriceOvershift(final Double retailPriceOvershift) {
        this.retailPriceOvershift = retailPriceOvershift;
    }

    public Double getNotOvershift() {
        return notOvershift;
    }

    public void setNotOvershift(final Double notOvershift) {
        this.notOvershift = notOvershift;
    }

    public Double getExciseTaxOvershift() {
        return exciseTaxOvershift;
    }

    public void setExciseTaxOvershift(final Double exciseTaxOvershift) {
        this.exciseTaxOvershift = exciseTaxOvershift;
    }

    public Double getVatOvershift() {
        return vatOvershift;
    }

    public void setVatOvershift(final Double vatOvershift) {
        this.vatOvershift = vatOvershift;
    }

    public Double getLevyOvershift() {
        return levyOvershift;
    }

    public void setLevyOvershift(final Double levyOvershift) {
        this.levyOvershift = levyOvershift;
    }

    public Double getLegalConsumptionUndershift() {
        return legalConsumptionUndershift;
    }

    public void setLegalConsumptionUndershift(final Double legalConsumptionUndershift) {
        this.legalConsumptionUndershift = legalConsumptionUndershift;
    }

    public Double getLegalConsumptionChangeUndershift() {
        return legalConsumptionChangeUndershift;
    }

    public void setLegalConsumptionChangeUndershift(final Double legalConsumptionChangeUndershift) {
        this.legalConsumptionChangeUndershift = legalConsumptionChangeUndershift;
    }

    public Double getConsumptionIllicitUndershift() {
        return consumptionIllicitUndershift;
    }

    public void setConsumptionIllicitUndershift(final Double consumptionIllicitUndershift) {
        this.consumptionIllicitUndershift = consumptionIllicitUndershift;
    }

    public Double getExciseRevUndershift() {
        return exciseRevUndershift;
    }

    public void setExciseRevUndershift(final Double exciseRevUndershift) {
        this.exciseRevUndershift = exciseRevUndershift;
    }

    public Double getExciseRevChangeUndershift() {
        return exciseRevChangeUndershift;
    }

    public void setExciseRevChangeUndershift(final Double exciseRevChangeUndershift) {
        this.exciseRevChangeUndershift = exciseRevChangeUndershift;
    }

    public Double getTotalGovRevUndershift() {
        return totalGovRevUndershift;
    }

    public void setTotalGovRevUndershift(final Double totalGovRevUndershift) {
        this.totalGovRevUndershift = totalGovRevUndershift;
    }

    public Double getExciseBurdenUndershift() {
        return exciseBurdenUndershift;
    }

    public void setExciseBurdenUndershift(final Double exciseBurdenUndershift) {
        this.exciseBurdenUndershift = exciseBurdenUndershift;
    }

    public Double getTotalTaxBurdenUndershift() {
        return totalTaxBurdenUndershift;
    }

    public void setTotalTaxBurdenUndershift(final Double totalTaxBurdenUndershift) {
        this.totalTaxBurdenUndershift = totalTaxBurdenUndershift;
    }

    public Double getRetailPriceUndershift() {
        return retailPriceUndershift;
    }

    public void setRetailPriceUndershift(final Double retailPriceUndershift) {
        this.retailPriceUndershift = retailPriceUndershift;
    }

    public Double getNotUndershift() {
        return notUndershift;
    }

    public void setNotUndershift(final Double notUndershift) {
        this.notUndershift = notUndershift;
    }

    public Double getExciseTaxUndershift() {
        return exciseTaxUndershift;
    }

    public void setExciseTaxUndershift(final Double exciseTaxUndershift) {
        this.exciseTaxUndershift = exciseTaxUndershift;
    }

    public Double getVatUndershift() {
        return vatUndershift;
    }

    public void setVatUndershift(final Double vatUndershift) {
        this.vatUndershift = vatUndershift;
    }

    public Double getLevyUndershift() {
        return levyUndershift;
    }

    public void setLevyUndershift(final Double levyUndershift) {
        this.levyUndershift = levyUndershift;
    }

    public Double getBaselineTotalTaxBurdenOvershift() {
        return baselineTotalTaxBurdenOvershift;
    }

    public void setBaselineTotalTaxBurdenOvershift(final Double baselineTotalTaxBurdenOvershift) {
        this.baselineTotalTaxBurdenOvershift = baselineTotalTaxBurdenOvershift;
    }

    public Double getBaselineTotalTaxBurdenUndershift() {
        return baselineTotalTaxBurdenUndershift;
    }

    public void setBaselineTotalTaxBurdenUndershift(final Double baselineTotalTaxBurdenUndershift) {
        this.baselineTotalTaxBurdenUndershift = baselineTotalTaxBurdenUndershift;
    }

    public Double getLegalConsumptionPerfectshift() {
        return legalConsumptionPerfectshift;
    }

    public void setLegalConsumptionPerfectshift(final Double legalConsumptionPerfectshift) {
        this.legalConsumptionPerfectshift = legalConsumptionPerfectshift;
    }

    public Double getLegalConsumptionChangePerfectshift() {
        return legalConsumptionChangePerfectshift;
    }

    public void setLegalConsumptionChangePerfectshift(final Double legalConsumptionChangePerfectshift) {
        this.legalConsumptionChangePerfectshift = legalConsumptionChangePerfectshift;
    }

    public Double getConsumptionIllicitPerfectshift() {
        return consumptionIllicitPerfectshift;
    }

    public void setConsumptionIllicitPerfectshift(final Double consumptionIllicitPerfectshift) {
        this.consumptionIllicitPerfectshift = consumptionIllicitPerfectshift;
    }

    public Double getExciseRevPerfectshift() {
        return exciseRevPerfectshift;
    }

    public void setExciseRevPerfectshift(final Double exciseRevPerfectshift) {
        this.exciseRevPerfectshift = exciseRevPerfectshift;
    }

    public Double getExciseRevChangePerfectshift() {
        return exciseRevChangePerfectshift;
    }

    public void setExciseRevChangePerfectshift(final Double exciseRevChangePerfectshift) {
        this.exciseRevChangePerfectshift = exciseRevChangePerfectshift;
    }

    public Double getTotalGovRevPerfectshift() {
        return totalGovRevPerfectshift;
    }

    public void setTotalGovRevPerfectshift(final Double totalGovRevPerfectshift) {
        this.totalGovRevPerfectshift = totalGovRevPerfectshift;
    }

    public Double getExciseBurdenPerfectshift() {
        return exciseBurdenPerfectshift;
    }

    public void setExciseBurdenPerfectshift(final Double exciseBurdenPerfectshift) {
        this.exciseBurdenPerfectshift = exciseBurdenPerfectshift;
    }

    public Double getTotalTaxBurdenPerfectshift() {
        return totalTaxBurdenPerfectshift;
    }

    public void setTotalTaxBurdenPerfectshift(final Double totalTaxBurdenPerfectshift) {
        this.totalTaxBurdenPerfectshift = totalTaxBurdenPerfectshift;
    }

    public Double getBaselineTotalTaxBurdenPerfectshift() {
        return baselineTotalTaxBurdenPerfectshift;
    }

    public void setBaselineTotalTaxBurdenPerfectshift(final Double baselineTotalTaxBurdenPerfectshift) {
        this.baselineTotalTaxBurdenPerfectshift = baselineTotalTaxBurdenPerfectshift;
    }

    public Double getRetailPricePerfectshift() {
        return retailPricePerfectshift;
    }

    public void setRetailPricePerfectshift(final Double retailPricePerfectshift) {
        this.retailPricePerfectshift = retailPricePerfectshift;
    }

    public Double getNotPerfectshift() {
        return notPerfectshift;
    }

    public void setNotPerfectshift(final Double notPerfectshift) {
        this.notPerfectshift = notPerfectshift;
    }

    public Double getExciseTaxPerfectshift() {
        return exciseTaxPerfectshift;
    }

    public void setExciseTaxPerfectshift(final Double exciseTaxPerfectshift) {
        this.exciseTaxPerfectshift = exciseTaxPerfectshift;
    }

    public Double getVatPerfectshift() {
        return vatPerfectshift;
    }

    public void setVatPerfectshift(final Double vatPerfectshift) {
        this.vatPerfectshift = vatPerfectshift;
    }

    public Double getLevyPerfectshift() {
        return levyPerfectshift;
    }

    public void setLevyPerfectshift(final Double levyPerfectshift) {
        this.levyPerfectshift = levyPerfectshift;
    }
}
