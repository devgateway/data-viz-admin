package org.devgateway.toolkit.persistence.dao.data;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TetsimDataset extends Dataset {

    @Audited
    BigDecimal cigaretteConsumption;

    @Audited
    BigDecimal vatRate;

    @Audited
    BigDecimal cigaretteDeclaredCustomValue;

    @Audited
    BigDecimal adultPopulation;

    @Audited
    BigDecimal smokingPrevalence;

    @OneToOne(fetch = FetchType.LAZY)
    @Audited
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    TetsimPriceVariable retailPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @Audited
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    TetsimPriceVariable marketShare;

    @OneToOne(fetch = FetchType.LAZY)
    @Audited
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    TetsimPriceVariable CIF;

    @OneToOne(fetch = FetchType.LAZY)
    @Audited
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    TetsimPriceVariable tobaccoLevy;

    @OneToOne(fetch = FetchType.LAZY)
    @Audited
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    TetsimPriceVariable exciseTax;

    @OneToOne(fetch = FetchType.LAZY)
    @Audited
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    TetsimPriceVariable customsDuty;

    @OneToOne(fetch = FetchType.LAZY)
    @Audited
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    TetsimPriceVariable elasticityOfDemandPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @Audited
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    TetsimPriceVariable elasticityOfDemandCrossPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @Audited
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    TetsimPriceVariable changeInIllicitNot;

    @OneToOne(fetch = FetchType.LAZY)
    @Audited
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    TetsimPriceVariable overShifting;

    @OneToOne(fetch = FetchType.LAZY)
    @Audited
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    TetsimPriceVariable underShifting;

    public BigDecimal getCigaretteConsumption() {
        return cigaretteConsumption;
    }

    public void setCigaretteConsumption(final BigDecimal cigaretteConsumption) {
        this.cigaretteConsumption = cigaretteConsumption;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(final BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getCigaretteDeclaredCustomValue() {
        return cigaretteDeclaredCustomValue;
    }

    public void setCigaretteDeclaredCustomValue(final BigDecimal cigaretteDeclaredCustomValue) {
        this.cigaretteDeclaredCustomValue = cigaretteDeclaredCustomValue;
    }

    public BigDecimal getAdultPopulation() {
        return adultPopulation;
    }

    public void setAdultPopulation(final BigDecimal adultPopulation) {
        this.adultPopulation = adultPopulation;
    }

    public BigDecimal getSmokingPrevalence() {
        return smokingPrevalence;
    }

    public void setSmokingPrevalence(final BigDecimal smokingPrevalence) {
        this.smokingPrevalence = smokingPrevalence;
    }

    public TetsimPriceVariable getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(final TetsimPriceVariable retailPrice) {
        this.retailPrice = retailPrice;
    }

    public TetsimPriceVariable getMarketShare() {
        return marketShare;
    }

    public void setMarketShare(final TetsimPriceVariable marketShare) {
        this.marketShare = marketShare;
    }

    public TetsimPriceVariable getCIF() {
        return CIF;
    }

    public void setCIF(final TetsimPriceVariable CIF) {
        this.CIF = CIF;
    }

    public TetsimPriceVariable getTobaccoLevy() {
        return tobaccoLevy;
    }

    public void setTobaccoLevy(final TetsimPriceVariable tobaccoLevy) {
        this.tobaccoLevy = tobaccoLevy;
    }

    public TetsimPriceVariable getExciseTax() {
        return exciseTax;
    }

    public void setExciseTax(final TetsimPriceVariable exciseTax) {
        this.exciseTax = exciseTax;
    }

    public TetsimPriceVariable getCustomsDuty() {
        return customsDuty;
    }

    public void setCustomsDuty(final TetsimPriceVariable customsDuty) {
        this.customsDuty = customsDuty;
    }

    public TetsimPriceVariable getElasticityOfDemandPrice() {
        return elasticityOfDemandPrice;
    }

    public void setElasticityOfDemandPrice(final TetsimPriceVariable elasticityOfDemandPrice) {
        this.elasticityOfDemandPrice = elasticityOfDemandPrice;
    }

    public TetsimPriceVariable getElasticityOfDemandCrossPrice() {
        return elasticityOfDemandCrossPrice;
    }

    public void setElasticityOfDemandCrossPrice(final TetsimPriceVariable elasticityOfDemandCrossPrice) {
        this.elasticityOfDemandCrossPrice = elasticityOfDemandCrossPrice;
    }

    public TetsimPriceVariable getChangeInIllicitNot() {
        return changeInIllicitNot;
    }

    public void setChangeInIllicitNot(final TetsimPriceVariable changeInIllicitNot) {
        this.changeInIllicitNot = changeInIllicitNot;
    }

    public TetsimPriceVariable getOverShifting() {
        return overShifting;
    }

    public void setOverShifting(final TetsimPriceVariable overShifting) {
        this.overShifting = overShifting;
    }

    public TetsimPriceVariable getUnderShifting() {
        return underShifting;
    }

    public void setUnderShifting(final TetsimPriceVariable underShifting) {
        this.underShifting = underShifting;
    }
}
