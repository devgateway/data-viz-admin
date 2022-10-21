package org.devgateway.toolkit.persistence.dto;

import org.devgateway.toolkit.persistence.dao.data.TobaccoProduct;

import java.io.Serializable;

/**
 *
 *
 * @author vchihai
 */
public class TetsimOutput implements Serializable {

    private Integer year;

    private Integer taxChange;

    private TobaccoProduct tobaccoProduct;

    private String shifting;

    private Double legalConsumption;

    private Double legalConsumptionChange;

    private Double consumptionIllicit;

    private Double exciseRev;

    private Double exciseRevChange;

    private Double totalGovRev;

    private Double exciseBurden;

    private Double totalTaxBurden;

    private Double retailPrice;

    private Double not;

    private Double exciseTax;

    private Double vat;

    private Double levy;

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

    public String getShifting() {
        return shifting;
    }

    public void setShifting(final String shifting) {
        this.shifting = shifting;
    }

    public Double getLegalConsumption() {
        return legalConsumption;
    }

    public void setLegalConsumption(final Double legalConsumption) {
        this.legalConsumption = legalConsumption;
    }

    public Double getConsumptionIllicit() {
        return consumptionIllicit;
    }

    public void setConsumptionIllicit(final Double consumptionIllicit) {
        this.consumptionIllicit = consumptionIllicit;
    }

    public Double getExciseRev() {
        return exciseRev;
    }

    public void setExciseRev(final Double exciseRev) {
        this.exciseRev = exciseRev;
    }

    public Double getTotalGovRev() {
        return totalGovRev;
    }

    public void setTotalGovRev(final Double totalGovRev) {
        this.totalGovRev = totalGovRev;
    }

    public Double getExciseBurden() {
        return exciseBurden;
    }

    public void setExciseBurden(final Double exciseBurden) {
        this.exciseBurden = exciseBurden;
    }

    public Double getTotalTaxBurden() {
        return totalTaxBurden;
    }

    public void setTotalTaxBurden(final Double totalTaxBurden) {
        this.totalTaxBurden = totalTaxBurden;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(final Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getNot() {
        return not;
    }

    public void setNot(final Double not) {
        this.not = not;
    }

    public Double getExciseTax() {
        return exciseTax;
    }

    public void setExciseTax(final Double exciseTax) {
        this.exciseTax = exciseTax;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(final Double vat) {
        this.vat = vat;
    }

    public Double getLevy() {
        return levy;
    }

    public void setLevy(final Double levy) {
        this.levy = levy;
    }

    public Double getLegalConsumptionChange() {
        return legalConsumptionChange;
    }

    public void setLegalConsumptionChange(final Double legalConsumptionChange) {
        this.legalConsumptionChange = legalConsumptionChange;
    }

    public Double getExciseRevChange() {
        return exciseRevChange;
    }

    public void setExciseRevChange(final Double exciseRevChange) {
        this.exciseRevChange = exciseRevChange;
    }
}
