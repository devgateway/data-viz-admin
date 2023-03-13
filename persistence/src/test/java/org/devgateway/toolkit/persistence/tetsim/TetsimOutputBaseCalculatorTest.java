package org.devgateway.toolkit.persistence.tetsim;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dao.data.TetsimPriceVariable;
import org.devgateway.toolkit.persistence.dao.data.TobaccoProduct;
import org.devgateway.toolkit.persistence.tetsim.builder.TetsimDatasetBuilder;
import org.devgateway.toolkit.persistence.tetsim.builder.TetsimPriceVariableBuilder;
import org.devgateway.toolkit.persistence.tetsim.builder.TetsimTobaccoProductValueBuilder;
import org.junit.Before;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.DISCOUNT;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.ILLICIT;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.IMPORTED;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.POPULAR;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.PREMIUM;

public class TetsimOutputBaseCalculatorTest {

    protected TetsimDataset datasetWithAllTobaccoProducts;

    double delta = 0.01;

    @Before
    public void setUp() {
        this.datasetWithAllTobaccoProducts = createTetsimDatasetWithAllTobaccoProducts();
    }

    protected TetsimDataset createTetsimDatasetWithAllTobaccoProducts() {
        return new TetsimDatasetBuilder()
                .withCigaretteConsumption(BigDecimal.valueOf(1150.0))
                .withVatRate(BigDecimal.valueOf(15))
                .withCigaretteDeclaredCustomValue(BigDecimal.valueOf(2.05))
                .withAdultPopulation(BigDecimal.valueOf(42571))
                .withSmokingPrevalence(BigDecimal.valueOf(16.9))
                .withRetailPrice(createPriceVariable(BigDecimal.valueOf(45), BigDecimal.valueOf(48), BigDecimal.valueOf(40), BigDecimal.valueOf(28), BigDecimal.valueOf(20)))
                .withMarketShare(createPriceVariable(BigDecimal.valueOf(5), BigDecimal.valueOf(7.6), BigDecimal.valueOf(40), BigDecimal.valueOf(13), BigDecimal.valueOf(34.4)))
                .withCif(createPriceVariable(BigDecimal.valueOf(2.05), BigDecimal.valueOf(2.05), BigDecimal.valueOf(2.05), BigDecimal.valueOf(2.05), BigDecimal.valueOf(2.05)))
                .withTobaccoLevy(createPriceVariable(ZERO, ZERO, ZERO, ZERO, ZERO))
                .withExciseTax(createPriceVariable(BigDecimal.valueOf(18.78), BigDecimal.valueOf(18.78), BigDecimal.valueOf(18.78), BigDecimal.valueOf(18.78), ZERO))
                .withCustomDuty(createPriceVariable(BigDecimal.valueOf(45), null, null, null, null))
                .withElasticityPrice(createPriceVariable(BigDecimal.valueOf(-0.4), BigDecimal.valueOf(-0.4), BigDecimal.valueOf(-0.6), BigDecimal.valueOf(-0.8), BigDecimal.valueOf(-0.6)))
                .withCrossElasticityPrice(createPriceVariable(ZERO, ZERO, BigDecimal.valueOf(0.2), BigDecimal.valueOf(0.3), BigDecimal.valueOf(0.3)))
                .withChangeInIllicitNot(createPriceVariable(null, null,null, null, BigDecimal.valueOf(0.5)))
                .withOvershifting(createPriceVariable(BigDecimal.valueOf(0.3), BigDecimal.valueOf(0.3), BigDecimal.valueOf(0.2), BigDecimal.valueOf(0.1), ZERO))
                .withUndershifting(createPriceVariable(BigDecimal.valueOf(-0.2), BigDecimal.valueOf(-0.2), BigDecimal.valueOf(-0.3), BigDecimal.valueOf(-0.15), ZERO))
                .build();
    }

    protected TetsimPriceVariable createPriceVariable(BigDecimal imported, BigDecimal premium, BigDecimal popular,
                                                    BigDecimal discount, BigDecimal illicit) {
        return new TetsimPriceVariableBuilder()
                .add(new TetsimTobaccoProductValueBuilder()
                        .withTobaccoProduct(IMPORTED)
                        .withValue(imported)
                        .build())
                .add(new TetsimTobaccoProductValueBuilder()
                        .withTobaccoProduct(PREMIUM)
                        .withValue(premium)
                        .build())
                .add(new TetsimTobaccoProductValueBuilder()
                        .withTobaccoProduct(POPULAR)
                        .withValue(popular)
                        .build())
                .add(new TetsimTobaccoProductValueBuilder()
                        .withTobaccoProduct(DISCOUNT)
                        .withValue(discount)
                        .build())
                .add(new TetsimTobaccoProductValueBuilder()
                        .withTobaccoProduct(ILLICIT)
                        .withValue(illicit)
                        .build())
                .build();
    }

}