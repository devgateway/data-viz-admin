package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;

/**
 *
 *
 * @author vchihai
 */
public class TetsimExportOutput implements Serializable {

    private Integer year;

    private Integer taxChange;

    private String tobaccoProduct;

    private Double legalConsumptionOvershift;

    private Double legalConsumptionChangeOvershift;

    private Double consumptionIllicitOvershift;

    private Double exciseRevOvershift;

    private Double exciseRevChangeOvershift;

    private Double totalGovRevOvershift;

    private Double exciseBurdenOvershift;

    private Double totalTaxBurdenOvershift;

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

    private Double retailPriceUndershift;

    private Double notUndershift;

    private Double exciseTaxUndershift;

    private Double vatUndershift;

    private Double levyUndershift;

    public TetsimExportOutput() {
    }

    public TetsimExportOutput(TetsimOutput overShfit, TetsimOutput underShift) {
        this.year = overShfit.getYear();
        this.taxChange = overShfit.getTaxChange();
        this.tobaccoProduct = overShfit.getTobaccoProduct();
        this.legalConsumptionOvershift = overShfit.getLegalConsumption();
        this.legalConsumptionChangeOvershift = overShfit.getLegalConsumptionChange();
        this.consumptionIllicitOvershift = overShfit.getConsumptionIllicit();
        this.exciseRevOvershift = overShfit.getExciseRev();
        this.exciseRevChangeOvershift = overShfit.getExciseRevChange();
        this.totalGovRevOvershift = overShfit.getTotalGovRev();
        this.exciseBurdenOvershift = overShfit.getExciseBurden();
        this.totalTaxBurdenOvershift = overShfit.getTotalTaxBurden();
        this.retailPriceOvershift = overShfit.getRetailPrice();
        this.notOvershift = overShfit.getNot();
        this.exciseTaxOvershift = overShfit.getExciseTax();
        this.vatOvershift = overShfit.getVat();
        this.levyOvershift = overShfit.getLevy();
        this.legalConsumptionUndershift = underShift.getLegalConsumption();
        this.legalConsumptionChangeUndershift = underShift.getLegalConsumptionChange();
        this.consumptionIllicitUndershift = underShift.getConsumptionIllicit();
        this.exciseRevUndershift = underShift.getExciseRev();
        this.exciseRevChangeUndershift = underShift.getExciseRevChange();
        this.totalGovRevUndershift = underShift.getTotalGovRev();
        this.exciseBurdenUndershift = underShift.getExciseBurden();
        this.totalTaxBurdenUndershift = underShift.getTotalTaxBurden();
        this.retailPriceUndershift = underShift.getRetailPrice();
        this.notUndershift = underShift.getNot();
        this.exciseTaxUndershift = underShift.getExciseTax();
        this.vatUndershift = underShift.getVat();
        this.levyUndershift = underShift.getLevy();
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

    public String getTobaccoProduct() {
        return tobaccoProduct;
    }

    public void setTobaccoProduct(final String tobaccoProduct) {
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

}
