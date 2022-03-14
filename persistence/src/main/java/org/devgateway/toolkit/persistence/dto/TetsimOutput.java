package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 *
 * @author vchihai
 */
public class TetsimOutput implements Serializable {

    private Integer baseline;

    private BigDecimal consumptionLegal;

    private BigDecimal consumptionIllicit;

    private BigDecimal exciseRev;

    private BigDecimal totalGovRev;

    private BigDecimal exciseBurden;

    private BigDecimal totalTaxBurden;

    private BigDecimal retailPrice;

    private BigDecimal not;

    private BigDecimal exciseTax;

    private BigDecimal vat;

    private BigDecimal levy;

    public Integer getBaseline() {
        return baseline;
    }

    public void setBaseline(final Integer baseline) {
        this.baseline = baseline;
    }

    public BigDecimal getConsumptionLegal() {
        return consumptionLegal;
    }

    public void setConsumptionLegal(final BigDecimal consumptionLegal) {
        this.consumptionLegal = consumptionLegal;
    }

    public BigDecimal getConsumptionIllicit() {
        return consumptionIllicit;
    }

    public void setConsumptionIllicit(final BigDecimal consumptionIllicit) {
        this.consumptionIllicit = consumptionIllicit;
    }

    public BigDecimal getExciseRev() {
        return exciseRev;
    }

    public void setExciseRev(final BigDecimal exciseRev) {
        this.exciseRev = exciseRev;
    }

    public BigDecimal getTotalGovRev() {
        return totalGovRev;
    }

    public void setTotalGovRev(final BigDecimal totalGovRev) {
        this.totalGovRev = totalGovRev;
    }

    public BigDecimal getExciseBurden() {
        return exciseBurden;
    }

    public void setExciseBurden(final BigDecimal exciseBurden) {
        this.exciseBurden = exciseBurden;
    }

    public BigDecimal getTotalTaxBurden() {
        return totalTaxBurden;
    }

    public void setTotalTaxBurden(final BigDecimal totalTaxBurden) {
        this.totalTaxBurden = totalTaxBurden;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(final BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getNot() {
        return not;
    }

    public void setNot(final BigDecimal not) {
        this.not = not;
    }

    public BigDecimal getExciseTax() {
        return exciseTax;
    }

    public void setExciseTax(final BigDecimal exciseTax) {
        this.exciseTax = exciseTax;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(final BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getLevy() {
        return levy;
    }

    public void setLevy(final BigDecimal levy) {
        this.levy = levy;
    }
}
