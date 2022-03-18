package org.devgateway.toolkit.forms.util.tetsim.builder;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dao.data.TetsimPriceVariable;

import java.math.BigDecimal;

public class TetsimDatasetBuilder {

    private TetsimDataset dataset;

    public TetsimDatasetBuilder() {
        dataset = new TetsimDataset();
    }

    public TetsimDatasetBuilder withCigaretteConsumption(BigDecimal consumption) {
        dataset.setCigaretteConsumption(consumption);
        return this;
    }

    public TetsimDatasetBuilder withCigaretteDeclaredCustomValue(BigDecimal declaredCustomValue) {
        dataset.setCigaretteDeclaredCustomValue(declaredCustomValue);
        return this;
    }

    public TetsimDatasetBuilder withAdultPopulation(BigDecimal adultPopulation) {
        dataset.setAdultPopulation(adultPopulation);
        return this;
    }

    public TetsimDatasetBuilder withVatRate(BigDecimal vatRate) {
        dataset.setVatRate(vatRate);
        return this;
    }

    public TetsimDatasetBuilder withSmokingPrevalence(BigDecimal smokingPrevalence) {
        dataset.setSmokingPrevalence(smokingPrevalence);
        return this;
    }

    public TetsimDatasetBuilder withRetailPrice(TetsimPriceVariable retailPrice) {
        dataset.setRetailPrice(retailPrice);
        return this;
    }

    public TetsimDatasetBuilder withMarketShare(TetsimPriceVariable marketShare) {
        dataset.setMarketShare(marketShare);
        return this;
    }

    public TetsimDatasetBuilder withCif(TetsimPriceVariable cif) {
        dataset.setCif(cif);
        return this;
    }

    public TetsimDatasetBuilder withTobaccoLevy(TetsimPriceVariable tobaccoLevy) {
        dataset.setTobaccoLevy(tobaccoLevy);
        return this;
    }

    public TetsimDatasetBuilder withExciseTax(TetsimPriceVariable exciseTax) {
        dataset.setExciseTax(exciseTax);
        return this;
    }

    public TetsimDatasetBuilder withCustomDuty(TetsimPriceVariable customDuty) {
        dataset.setCustomsDuty(customDuty);
        return this;
    }

    public TetsimDatasetBuilder withElasticityPrice(TetsimPriceVariable elasticityPrice) {
        dataset.setElasticityOfDemandPrice(elasticityPrice);
        return this;
    }

    public TetsimDatasetBuilder withCrossElasticityPrice(TetsimPriceVariable crossElasticityPrice) {
        dataset.setElasticityOfDemandCrossPrice(crossElasticityPrice);
        return this;
    }

    public TetsimDatasetBuilder withChangeInIllicitNot(TetsimPriceVariable changeInIllicitNot) {
        dataset.setChangeInIllicitNot(changeInIllicitNot);
        return this;
    }

    public TetsimDatasetBuilder withOvershifting(TetsimPriceVariable overshifting) {
        dataset.setOvershifting(overshifting);
        return this;
    }

    public TetsimDatasetBuilder withUndershifting(TetsimPriceVariable undershifting) {
        dataset.setUndershifting(undershifting);
        return this;
    }

    public TetsimDataset build() {
        return dataset;
    }

}
