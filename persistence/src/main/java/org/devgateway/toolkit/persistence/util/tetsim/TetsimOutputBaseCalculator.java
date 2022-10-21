package org.devgateway.toolkit.persistence.util.tetsim;

import com.google.common.collect.ImmutableMap;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dao.data.TobaccoProduct;
import org.devgateway.toolkit.persistence.dto.TetsimOutput;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.DISCOUNT;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.ILLICIT;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.IMPORTED;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.POPULAR;
import static org.devgateway.toolkit.persistence.dao.data.TobaccoProduct.PREMIUM;
import static org.devgateway.toolkit.persistence.util.tetsim.TetsimUtil.getLegalTobaccoProducts;
import static org.devgateway.toolkit.persistence.util.tetsim.TetsimUtil.getTobaccoProductValueFromVariable;

/**
 * @author vchihai
 */
public abstract class TetsimOutputBaseCalculator implements TetsimOutputCalculator {

    public static final BigDecimal HUNDRED = TEN.scaleByPowerOfTen(1);

    public static final MathContext DEFAULT_CONTEXT = MathContext.DECIMAL64;

    protected final TetsimDataset dataset;

    protected final Integer percentageChange;

    protected final Double trackTracingAdditionalCost;

    protected final Double levyPercentageChange;

    protected final Double changeInTheDeclaredCustomValue = 0.0;

    public static final Map<TobaccoProduct, TobaccoProduct> GAIN_SWITCH_TOBACCO_PRODUCT = new ImmutableMap.Builder()
            .put(POPULAR, PREMIUM)
            .put(DISCOUNT, POPULAR)
            .put(ILLICIT, DISCOUNT)
            .build();

    public static final Map<TobaccoProduct, TobaccoProduct> CONSUMPTION_DEPENDENT_TOBACCO_PRODUCT = new ImmutableMap.Builder()
            .put(IMPORTED, PREMIUM)
            .put(PREMIUM, POPULAR)
            .put(POPULAR, DISCOUNT)
            .put(DISCOUNT, ILLICIT)
            .build();

    public TetsimOutputBaseCalculator(TetsimDataset dataset, Integer percentageChange) {
        this(dataset, percentageChange, 0.0, 0.0);
    }

    public TetsimOutputBaseCalculator(TetsimDataset dataset, Integer percentageChange,
                                      Double trackTracingAdditionalCost, Double levyPercentageChange) {
        this.dataset = dataset;
        this.percentageChange = percentageChange;
        this.trackTracingAdditionalCost = trackTracingAdditionalCost;
        this.levyPercentageChange = levyPercentageChange;
    }

    protected abstract String getShifting();

    protected abstract BigDecimal calculateAbsChangeShift(TobaccoProduct tobaccoProduct);

    @Override
    public TetsimOutput calculate(TobaccoProduct tobaccoProduct) {
        TetsimOutput tetsimOutput = new TetsimOutput();

        tetsimOutput.setYear(this.dataset.getYear());
        tetsimOutput.setShifting(getShifting());
        tetsimOutput.setTobaccoProduct(tobaccoProduct);
        tetsimOutput.setTaxChange(percentageChange.intValue());
        tetsimOutput.setLegalConsumption(calculateOutputLegalConsumption());
        tetsimOutput.setLegalConsumptionChange(calculateOutputLegalConsumptionChange());
        tetsimOutput.setConsumptionIllicit(calculateOutputConsumptionIllicit());
        tetsimOutput.setExciseRev(calculateOutputExciseRev());
        tetsimOutput.setExciseRevChange(calculateOutputExciseRevChange());
        tetsimOutput.setTotalGovRev(calculateOutputTotalGovRev());

        tetsimOutput.setExciseBurden(calculateOutputExciseBurden(tobaccoProduct));
        tetsimOutput.setTotalTaxBurden(calculateOutputTotalTaxBurden(tobaccoProduct));
        tetsimOutput.setRetailPrice(calculateOutputRetailPrice(tobaccoProduct));
        tetsimOutput.setNot(calculateOutputNot(tobaccoProduct));
        tetsimOutput.setExciseTax(calculateOutputExciseTax(tobaccoProduct));
        tetsimOutput.setVat(calculateOutputVat(tobaccoProduct));
        tetsimOutput.setLevy(calculateOutputLevy(tobaccoProduct));

        return tetsimOutput;
    }

    public Double calculateOutputLegalConsumption() {
        return calculateTotalLegalConsumption().doubleValue();
    }

    public Double calculateOutputLegalConsumptionChange() {
        return calculateTotalLegalConsumptionChange().doubleValue();
    }

    private Double calculateOutputConsumptionIllicit() {
        return calculateConsumption(ILLICIT).doubleValue();
    }

    private Double calculateOutputExciseRev() {
        return calculateTotalExciseRevenue().doubleValue();
    }

    private Double calculateOutputExciseRevChange() {
        return calculateTotalExciseRevenueChange().doubleValue();
    }

    private Double calculateOutputTotalGovRev() {
        return calculateTotalLegalGovernmentRevenue().doubleValue();
    }

    /**
     * Calculate the Output Excise Burden
     */
    private Double calculateOutputExciseBurden(TobaccoProduct tobaccoProduct) {
        return calculateExciseBurden(tobaccoProduct).doubleValue();
    }

    /**
     * Calculate the Output Total Tax Burden
     */
    private Double calculateOutputTotalTaxBurden(TobaccoProduct tobaccoProduct) {
        return calculateTotalTaxBurden(tobaccoProduct).doubleValue();
    }

    /**
     * Calculate the Output Retail Price
     */
    public Double calculateOutputRetailPrice(TobaccoProduct tobaccoProduct) {
        return calculateRetailPrice(tobaccoProduct).doubleValue();
    }

    /**
     * Calculate the Output NOT
     */
    public Double calculateOutputNot(TobaccoProduct tobaccoProduct) {
        return calculateNetTaxPrice(tobaccoProduct).doubleValue();
    }

    /**
     * Calculate the Output Excise Tax
     */
    public Double calculateOutputExciseTax(TobaccoProduct tobaccoProduct) {
        return calculateExciseTaxDomesticProduction(tobaccoProduct).doubleValue();
    }

    /**
     * Calculate the Output VAT
     */
    public Double calculateOutputVat(TobaccoProduct tobaccoProduct) {
        return calculateVat(tobaccoProduct).doubleValue();
    }

    /**
     * Calculate the Output Levy
     */
    public Double calculateOutputLevy(TobaccoProduct tobaccoProduct) {
        return calculateTobaccoLevy(tobaccoProduct).doubleValue();
    }

    /**
     * Calculates Net of Tax Price
     *
     * @param tobaccoProduct
     */
    public BigDecimal calculateNetTaxPrice(TobaccoProduct tobaccoProduct) {
        BigDecimal netTaxPrice = calculateBaselineNetTaxPrice(tobaccoProduct);
        if (tobaccoProduct.equals(ILLICIT)) {
            BigDecimal shift = getTobaccoProductValueFromVariable(dataset.getChangeInIllicitNot(),
                    tobaccoProduct);
            BigDecimal discountRetailPriceDifference = calculateRetailPrice(DISCOUNT)
                    .subtract(calculateBaselineRetailPrice(DISCOUNT));
            return netTaxPrice.add(shift.multiply(discountRetailPriceDifference));
        }

        return netTaxPrice.add(calculateAbsChangeShift(tobaccoProduct));
    }

    /**
     * Calculates Additional charges related to Track and Tracing
     */
    private BigDecimal calculateAdditionalCharges() {
        return BigDecimal.valueOf(trackTracingAdditionalCost);
    }

    /**
     * Calculate Tobacco levy
     * CIF per pack * (levy rate + percentage change in levy) / 100
     */
    protected BigDecimal calculateTobaccoLevy(TobaccoProduct tobaccoProduct) {
        BigDecimal cif = getTobaccoProductValueFromVariable(dataset.getCif(), tobaccoProduct);
        BigDecimal levy = getTobaccoProductValueFromVariable(dataset.getTobaccoLevy(), tobaccoProduct);

        return cif.multiply(levy.add(BigDecimal.valueOf(levyPercentageChange)))
                .divide(HUNDRED, DEFAULT_CONTEXT);
    };

    /**
     * Excise tax from baseline * (1 + percent increase in excise tax/100)
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateExciseTaxDomesticProduction(TobaccoProduct tobaccoProduct) {
        return calculateBaselineExciseTaxOnDomesticProduction(tobaccoProduct)
                .multiply(ONE.add(BigDecimal.valueOf(percentageChange).divide(HUNDRED, DEFAULT_CONTEXT)));
    }

    /**
     * Excise tax from baseline * (1 + percent increase in excise tax/100)
     *
     * @param tobaccoProduct
     * @return
     */
    protected BigDecimal calculateExciseTaxImportedCigarettes(TobaccoProduct tobaccoProduct) {
        if (tobaccoProduct.equals(IMPORTED)) {
            return calculateBaselineExciseTaxOnImported(tobaccoProduct)
                    .multiply(ONE.add(BigDecimal.valueOf(percentageChange).divide(HUNDRED, DEFAULT_CONTEXT)));
        }

        return ZERO;
    }

    /**
     * Customs value from baseline * (1 + percent increase in customs rate/100)
     *
     * @param tobaccoProduct
     * @return
     */
    protected BigDecimal calculateCustomsDutyImportedCigarettes(TobaccoProduct tobaccoProduct) {
        if (tobaccoProduct.equals(IMPORTED)) {
            return calculateBaselineCustomsDuty(tobaccoProduct)
                    .multiply(
                            ONE.add(BigDecimal.valueOf(changeInTheDeclaredCustomValue).divide(HUNDRED, DEFAULT_CONTEXT))
                    );
        }

        return ZERO;
    }

    /**
     * Calculate the VAT with tax change
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateVat(TobaccoProduct tobaccoProduct) {
        if (tobaccoProduct.equals(ILLICIT)) {
            return ZERO;
        }

        BigDecimal vatRate = dataset.getVatRate();

        return calculateNetTaxPrice(tobaccoProduct)
                .add(calculateAdditionalCharges())
                .add(calculateTobaccoLevy(tobaccoProduct))
                .add(calculateExciseTaxDomesticProduction(tobaccoProduct))
                .add(calculateExciseTaxImportedCigarettes(tobaccoProduct))
                .add(calculateCustomsDutyImportedCigarettes(tobaccoProduct))
                .multiply(vatRate)
                .divide(HUNDRED, DEFAULT_CONTEXT);
    }

    /**
     * Calculate the Retail Price with tax change
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateRetailPrice(TobaccoProduct tobaccoProduct) {
        return calculateNetTaxPrice(tobaccoProduct)
                .add(calculateAdditionalCharges())
                .add(calculateTobaccoLevy(tobaccoProduct))
                .add(calculateExciseTaxDomesticProduction(tobaccoProduct))
                .add(calculateExciseTaxImportedCigarettes(tobaccoProduct))
                .add(calculateCustomsDutyImportedCigarettes(tobaccoProduct))
                .add(calculateVat(tobaccoProduct));
    }

    /**
     * Calculate the Excise Burden with tax change
     * excise tax/price * 100
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateExciseBurden(TobaccoProduct tobaccoProduct) {
        return calculateTobaccoLevy(tobaccoProduct)
                .add(calculateExciseTaxDomesticProduction(tobaccoProduct))
                .add(calculateExciseTaxImportedCigarettes(tobaccoProduct))
                .divide(calculateRetailPrice(tobaccoProduct), DEFAULT_CONTEXT)
                .multiply(HUNDRED);
    }

    /**
     * Calculate the Total Tax Burden with tax change
     * all tax/price * 100
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateTotalTaxBurden(TobaccoProduct tobaccoProduct) {
        return calculateTobaccoLevy(tobaccoProduct)
                .add(calculateExciseTaxDomesticProduction(tobaccoProduct))
                .add(calculateExciseTaxImportedCigarettes(tobaccoProduct))
                .add(calculateVat(tobaccoProduct))
                .divide(calculateRetailPrice(tobaccoProduct), DEFAULT_CONTEXT)
                .multiply(HUNDRED);
    }

    /**
     * Calculate Consumption (million packs):
     * Baseline Consumption *([1+price_elast*(price AT-price BT)/(price AT + price BT)] /
     *                        [1-price_elast*(price AT-price BT)/(price AT + price BT)]
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculatePreConsumption(TobaccoProduct tobaccoProduct) {
        BigDecimal baselineConsumption = calculateBaselineConsumption(tobaccoProduct);
        BigDecimal priceElasticity = getTobaccoProductValueFromVariable(dataset.getElasticityOfDemandPrice(),
                tobaccoProduct);

        BigDecimal baselineRetailPrice = calculateBaselineRetailPrice(tobaccoProduct);
        BigDecimal retailPrice = calculateRetailPrice(tobaccoProduct);

        BigDecimal diffRetailPrice = retailPrice.subtract(baselineRetailPrice);
        BigDecimal sumRetailPrice = retailPrice.add(baselineRetailPrice);
        BigDecimal multiplyElasticityRetail = priceElasticity.multiply(diffRetailPrice)
                .divide(sumRetailPrice, DEFAULT_CONTEXT);

        return baselineConsumption
                .multiply(ONE.add(multiplyElasticityRetail))
                .divide(ONE.subtract(multiplyElasticityRetail), DEFAULT_CONTEXT);
    }

    /**
     * Calculate Gain from higher price category
     * As prices change, some consumers decide to shift to cheaper products in order to maintain consumption.
     * This calculates these shifts between segments. We assume imported is quite a unique category,
     * so there is no shifting in/out of it. The others interact.
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateGainFromPrice(TobaccoProduct tobaccoProduct) {
        if (GAIN_SWITCH_TOBACCO_PRODUCT.containsKey(tobaccoProduct)) {
            TobaccoProduct switchProduct = GAIN_SWITCH_TOBACCO_PRODUCT.get(tobaccoProduct);

            BigDecimal consumption = calculatePreConsumption(tobaccoProduct);
            BigDecimal crossElasticity = getTobaccoProductValueFromVariable(dataset.getElasticityOfDemandCrossPrice(),
                    tobaccoProduct);

            BigDecimal baselineRetailPrice = calculateBaselineRetailPrice(switchProduct);
            BigDecimal retailPrice = calculateRetailPrice(switchProduct);

            BigDecimal diffRetailPrice = retailPrice.subtract(baselineRetailPrice);
            BigDecimal sumRetailPrice = retailPrice.add(baselineRetailPrice);
            BigDecimal multiplyElastRetail = crossElasticity.multiply(diffRetailPrice).divide(sumRetailPrice,
                    DEFAULT_CONTEXT);

            return consumption
                    .multiply(ONE.add(multiplyElastRetail))
                    .divide(ONE.subtract(multiplyElastRetail), DEFAULT_CONTEXT)
                    .subtract(consumption);
        }

        return ZERO;
    }

    /**
     * Calculate the consumption
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateConsumption(TobaccoProduct tobaccoProduct) {
        BigDecimal consumption = calculatePreConsumption(tobaccoProduct);
        BigDecimal gainFromPrice = calculateGainFromPrice(tobaccoProduct);

        if (CONSUMPTION_DEPENDENT_TOBACCO_PRODUCT.containsKey(tobaccoProduct)) {
            BigDecimal gainFromPriceDependent = calculateGainFromPrice(
                    CONSUMPTION_DEPENDENT_TOBACCO_PRODUCT.get(tobaccoProduct));
            return consumption.add(gainFromPrice).subtract(gainFromPriceDependent);
        }

        return consumption.add(gainFromPrice);
    }

    /**
     * Calculate the Total Legal Consumption (million packs): sum of all products except illicit
     * @return
     */
    public BigDecimal calculateTotalLegalConsumption() {
        return getLegalTobaccoProducts().stream()
                .map(tobaccoProduct -> calculateConsumption(tobaccoProduct))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalLegalConsumptionChange() {
        BigDecimal totalLegalConsumption = calculateTotalLegalConsumption();
        BigDecimal totalLegalConsumptionBaseline = calculateBaselineTotalLegalConsumption();
        return totalLegalConsumption.divide(totalLegalConsumptionBaseline, DEFAULT_CONTEXT)
                .subtract(ONE)
                .multiply(HUNDRED);
    }

    /**
     * Calculate Revenue Tobacco levy: Levy per pack AT * consumption in mill packs
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateRevenueTobaccoLevy(TobaccoProduct tobaccoProduct) {
        return calculateConsumption(tobaccoProduct).multiply(calculateTobaccoLevy(tobaccoProduct));
    }


    /**
     * Calculate Revenue Excise tax on domestic production: excise tax per pack AT * consumption in mill packs
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateRevenueExciseTaxDomesticProduction(TobaccoProduct tobaccoProduct) {
        return calculateConsumption(tobaccoProduct).multiply(calculateExciseTaxDomesticProduction(tobaccoProduct));
    }

    public BigDecimal calculateBaselineExciseRevenueDomesticProduction(TobaccoProduct tobaccoProduct) {
        return calculateBaselineConsumption(tobaccoProduct)
                .multiply(calculateBaselineExciseTaxOnDomesticProduction(tobaccoProduct));
    }

    public BigDecimal calculateBaselineExciseRevenueImportedCigarettes(TobaccoProduct tobaccoProduct) {
        return calculateBaselineConsumption(tobaccoProduct)
                .multiply(calculateBaselineExciseTaxOnImported(tobaccoProduct));
    }

    /**
     * Calculate Revenue Excise tax on imported production: excise tax (imported) per pack AT * consumption
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateRevenueExciseTaxImportedCigarettes(TobaccoProduct tobaccoProduct) {
        return calculateConsumption(tobaccoProduct).multiply(calculateExciseTaxImportedCigarettes(tobaccoProduct));
    }

    /**
     * Calculate Revenue Customs Duty on Imported Cigarettes: customs per pack * consumption
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateRevenueCustomsDutyImportedCigarettes(TobaccoProduct tobaccoProduct) {
        return calculateConsumption(tobaccoProduct).multiply(calculateCustomsDutyImportedCigarettes(tobaccoProduct));
    }

    /**
     * Calculate Revenue VAT: VAT per pack * consumption
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateRevenueVat(TobaccoProduct tobaccoProduct) {
        return calculateConsumption(tobaccoProduct).multiply(calculateVat(tobaccoProduct));
    }

    /**
     * Calculate Total Excise TaxesRevenue
     *
     * @param tobaccoProduct
     */
    public BigDecimal calculateTotalExciseTaxesRevenue(TobaccoProduct tobaccoProduct) {
        return calculateRevenueExciseTaxDomesticProduction(tobaccoProduct)
                .add(calculateRevenueExciseTaxImportedCigarettes(tobaccoProduct));
    }

    public BigDecimal calculateBaselineTotalExciseTaxesRevenue(TobaccoProduct tobaccoProduct) {
        return calculateBaselineExciseRevenueDomesticProduction(tobaccoProduct)
                .add(calculateBaselineExciseRevenueImportedCigarettes(tobaccoProduct));
    }

    /**
     * Calculate Total Excise Revenue: sum of excise tax
     */
    public BigDecimal calculateTotalExciseRevenue() {
        return getLegalTobaccoProducts().stream()
                .map(tobaccoProduct -> calculateTotalExciseTaxesRevenue(tobaccoProduct))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalExciseRevenueChange() {
        BigDecimal totalExciseRevenue = calculateTotalExciseRevenue();
        BigDecimal totalExciseRevenueBaseline = calculateBaselineTotalExciseRevenue();
        return totalExciseRevenue.divide(totalExciseRevenueBaseline, DEFAULT_CONTEXT)
                .subtract(ONE)
                .multiply(HUNDRED);
    }

    /**
     * Calculate Total Revenue
     *
     * @param tobaccoProduct
     */
    public BigDecimal calculateTotalRevenue(TobaccoProduct tobaccoProduct) {
        return calculateRevenueTobaccoLevy(tobaccoProduct)
                .add(calculateRevenueExciseTaxDomesticProduction(tobaccoProduct))
                .add(calculateRevenueExciseTaxImportedCigarettes(tobaccoProduct))
                .add(calculateRevenueCustomsDutyImportedCigarettes(tobaccoProduct))
                .add(calculateRevenueVat(tobaccoProduct));
    }

    /**
     * Calculate Total Government Revenue: excise tax + VAT + levy + customs
     */
    public BigDecimal calculateTotalLegalGovernmentRevenue() {
        return getLegalTobaccoProducts().stream()
                .map(tobaccoProduct -> calculateTotalRevenue(tobaccoProduct))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public BigDecimal calculateBaselineTobaccoLevy(TobaccoProduct tobaccoProduct) {
        BigDecimal cif = getTobaccoProductValueFromVariable(dataset.getCif(), tobaccoProduct);
        BigDecimal levy = getTobaccoProductValueFromVariable(dataset.getTobaccoLevy(), tobaccoProduct);

        return cif.multiply(levy)
                .divide(HUNDRED);
    }

    /**
     *  calculate CIF * levy / 100
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateBaselineExciseTaxOnDomesticProduction(TobaccoProduct tobaccoProduct) {
        if (tobaccoProduct.equals(IMPORTED)) {
            return ZERO;
        }

        return getTobaccoProductValueFromVariable(dataset.getExciseTax(), tobaccoProduct);
    }

    /**
     *  If tobacco product is imported, return excise tax
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateBaselineExciseTaxOnImported(TobaccoProduct tobaccoProduct) {
        if (tobaccoProduct.equals(IMPORTED)) {
            return getTobaccoProductValueFromVariable(dataset.getExciseTax(), tobaccoProduct);
        }

        return ZERO;
    }

    /**
     *  If tobacco product is imported, calculate CIF * customs duty / 100
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateBaselineCustomsDuty(TobaccoProduct tobaccoProduct) {
        if (tobaccoProduct.equals(IMPORTED)) {
            BigDecimal cif = getTobaccoProductValueFromVariable(dataset.getCif(), tobaccoProduct);
            BigDecimal duty = getTobaccoProductValueFromVariable(dataset.getCustomsDuty(), tobaccoProduct);

            if (duty == null) {
                return ZERO;
            }

            return cif.multiply(duty)
                    .divide(HUNDRED);
        }

        return ZERO;
    }

    /**
     * Calculate the Baseline Rate Price
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateBaselineRetailPrice(TobaccoProduct tobaccoProduct) {
        return getTobaccoProductValueFromVariable(dataset.getRetailPrice(), tobaccoProduct);
    }

    /**
     * Calculate baseeline VAT: Baseline Retail Price * VAT rate / (100 + VAT rate)
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateBaselineVAT(TobaccoProduct tobaccoProduct) {
        BigDecimal baselineRetailPrice = calculateBaselineRetailPrice(tobaccoProduct);
        BigDecimal vatRate = dataset.getVatRate();

        return baselineRetailPrice.multiply(vatRate)
                .divide(HUNDRED.add(vatRate), DEFAULT_CONTEXT);
    }

    /**
     * Calculates the industry component of the price (the price minus all taxes).
     * The first term backward calculates VAT, as price*(1+(VATrate/100)).
     * It then minuses excise tax and customs and the levy
     *
     * Note: illicit segment differs, it is just the retail price
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateBaselineNetTaxPrice(TobaccoProduct tobaccoProduct) {
        BigDecimal baselineRetailPrice = calculateBaselineRetailPrice(tobaccoProduct);

        if (tobaccoProduct.equals(ILLICIT)) {
            return baselineRetailPrice;
        }

        return baselineRetailPrice
                .divide(ONE.add(dataset.getVatRate().divide(HUNDRED)), DEFAULT_CONTEXT)
                .subtract(calculateBaselineTobaccoLevy(tobaccoProduct))
                .subtract(calculateBaselineExciseTaxOnDomesticProduction(tobaccoProduct))
                .subtract(calculateBaselineExciseTaxOnImported(tobaccoProduct))
                .subtract(calculateBaselineCustomsDuty(tobaccoProduct));
    }

    /**
     * Calculates excise burden: (excise) / retail price * 100
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateBaselineExciseBurden(TobaccoProduct tobaccoProduct) {
        BigDecimal baselineTobaccoLevy = calculateBaselineTobaccoLevy(tobaccoProduct);

        return baselineTobaccoLevy
                .add(calculateBaselineExciseTaxOnDomesticProduction(tobaccoProduct))
                .add(calculateBaselineExciseTaxOnImported(tobaccoProduct))
                .divide(calculateBaselineRetailPrice(tobaccoProduct), DEFAULT_CONTEXT)
                .multiply(HUNDRED);
    }

    /**
     *  Calculates total tax burden: (excise + VAT + customs) / retail price * 100
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateBaselineTotalTaxBurden(TobaccoProduct tobaccoProduct) {
        BigDecimal baselineTobaccoLevy = calculateBaselineTobaccoLevy(tobaccoProduct);

        return baselineTobaccoLevy
                .add(calculateBaselineExciseTaxOnDomesticProduction(tobaccoProduct))
                .add(calculateBaselineExciseTaxOnImported(tobaccoProduct))
                .add(calculateBaselineCustomsDuty(tobaccoProduct))
                .add(calculateBaselineVAT(tobaccoProduct))
                .divide(calculateBaselineRetailPrice(tobaccoProduct), DEFAULT_CONTEXT)
                .multiply(HUNDRED);
    }

    /**
     * Calculate Baseline Consumption (million packs): total market * share of market for each segment / 100
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateBaselineConsumption(TobaccoProduct tobaccoProduct) {
        BigDecimal consumption = dataset.getCigaretteConsumption();
        BigDecimal marketShare = getTobaccoProductValueFromVariable(dataset.getMarketShare(), tobaccoProduct);

        return consumption
                .multiply(marketShare)
                .divide(HUNDRED, DEFAULT_CONTEXT);
    }

    /**
     * Calculate the Baseline Total Consumption (million packs): summ of all products except illicit
     * @return
     */
    public BigDecimal calculateBaselineTotalLegalConsumption() {
        return getLegalTobaccoProducts().stream()
                .map(tobaccoProduct -> calculateBaselineConsumption(tobaccoProduct))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateBaselineTotalExciseRevenue() {
        return getLegalTobaccoProducts().stream()
                .map(tobaccoProduct -> calculateBaselineTotalExciseTaxesRevenue(tobaccoProduct))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
