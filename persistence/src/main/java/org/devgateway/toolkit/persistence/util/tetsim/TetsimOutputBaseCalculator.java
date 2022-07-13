package org.devgateway.toolkit.persistence.util.tetsim;

import com.google.common.collect.ImmutableMap;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dto.TetsimOutput;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
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

    public static final Map<String, String> GAIN_SWITCH_TOBACCO_PRODUCT = new ImmutableMap.Builder()
            .put("Popular", "Premium")
            .put("Discount", "Popular")
            .put("Illicit", "Discount")
            .build();

    public static final Map<String, String> CONSUMPTION_DEPENDENT_TOBACCO_PRODUCT = new ImmutableMap.Builder()
            .put("Imported", "Premium")
            .put("Premium", "Popular")
            .put("Popular", "Discount")
            .put("Discount", "Illicit")
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

    protected abstract BigDecimal calculateAbsChangeShift(String tobaccoProduct);

    @Override
    public TetsimOutput calculate(String tobaccoProduct) {
        TetsimOutput tetsimOutput = new TetsimOutput();

        tetsimOutput.setYear(this.dataset.getYear());
        tetsimOutput.setShifting(getShifting());
        tetsimOutput.setTobaccoProduct(tobaccoProduct);
        tetsimOutput.setTaxChange(percentageChange.intValue());
        tetsimOutput.setConsumptionLegal(calculateOutputConsumptionLegal());
        tetsimOutput.setConsumptionIllicit(calculateOutputConsumptionIllicit());
        tetsimOutput.setExciseRev(calculateOutputExciseRev());
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

    public Double calculateOutputConsumptionLegal() {
        return calculateTotalLegalConsumption().doubleValue();
    }

    private Double calculateOutputConsumptionIllicit() {
        return calculateConsumption("Illicit").doubleValue();
    }

    private Double calculateOutputExciseRev() {
        return calculateTotalExciseRevenue().doubleValue();
    }

    private Double calculateOutputTotalGovRev() {
        return calculateTotalLegalGovernmentRevenue().doubleValue();
    }

    /**
     * Calculate the Output Excise Burden
     */
    private Double calculateOutputExciseBurden(String tobaccoProduct) {
        return calculateExciseBurden(tobaccoProduct).doubleValue();
    }

    /**
     * Calculate the Output Total Tax Burden
     */
    private Double calculateOutputTotalTaxBurden(String tobaccoProduct) {
        return calculateTotalTaxBurden(tobaccoProduct).doubleValue();
    }

    /**
     * Calculate the Output Retail Price
     */
    public Double calculateOutputRetailPrice(String tobaccoProduct) {
        return calculateRetailPrice(tobaccoProduct).doubleValue();
    }

    /**
     * Calculate the Output NOT
     */
    public Double calculateOutputNot(String tobaccoProduct) {
        return calculateNetTaxPrice(tobaccoProduct).doubleValue();
    }

    /**
     * Calculate the Output Excise Tax
     */
    public Double calculateOutputExciseTax(String tobaccoProduct) {
        return calculateExciseTaxDomesticProduction(tobaccoProduct).doubleValue();
    }

    /**
     * Calculate the Output VAT
     */
    public Double calculateOutputVat(String tobaccoProduct) {
        return calculateVat(tobaccoProduct).doubleValue();
    }

    /**
     * Calculate the Output Levy
     */
    public Double calculateOutputLevy(String tobaccoProduct) {
        return calculateTobaccoLevy(tobaccoProduct).doubleValue();
    }

    /**
     * Calculates Net of Tax Price
     *
     * @param tobaccoProduct
     */
    public BigDecimal calculateNetTaxPrice(String tobaccoProduct) {
        BigDecimal netTaxPrice = calculateBaselineNetTaxPrice(tobaccoProduct);
        if (tobaccoProduct.equals("Illicit")) {
            BigDecimal shift = getTobaccoProductValueFromVariable(dataset.getChangeInIllicitNot(),
                    tobaccoProduct);
            BigDecimal discountRetailPriceDifference = calculateRetailPrice("Discount")
                    .subtract(calculateBaselineRetailPrice("Discount"));
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
    protected BigDecimal calculateTobaccoLevy(String tobaccoProduct) {
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
    public BigDecimal calculateExciseTaxDomesticProduction(String tobaccoProduct) {
        return calculateBaselineExciseTaxOnDomesticProduction(tobaccoProduct)
                .multiply(ONE.add(BigDecimal.valueOf(percentageChange).divide(HUNDRED, DEFAULT_CONTEXT)));
    }

    /**
     * Excise tax from baseline * (1 + percent increase in excise tax/100)
     *
     * @param tobaccoProduct
     * @return
     */
    protected BigDecimal calculateExciseTaxImportedCigarettes(String tobaccoProduct) {
        if (tobaccoProduct.equals("Imported")) {
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
    protected BigDecimal calculateCustomsDutyImportedCigarettes(String tobaccoProduct) {
        if (tobaccoProduct.equals("Imported")) {
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
    public BigDecimal calculateVat(String tobaccoProduct) {
        if (tobaccoProduct.equals("Illicit")) {
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
    public BigDecimal calculateRetailPrice(String tobaccoProduct) {
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
    public BigDecimal calculateExciseBurden(String tobaccoProduct) {
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
    public BigDecimal calculateTotalTaxBurden(String tobaccoProduct) {
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
    public BigDecimal calculatePreConsumption(String tobaccoProduct) {
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
    public BigDecimal calculateGainFromPrice(String tobaccoProduct) {
        if (GAIN_SWITCH_TOBACCO_PRODUCT.containsKey(tobaccoProduct)) {
            String switchProduct = GAIN_SWITCH_TOBACCO_PRODUCT.get(tobaccoProduct);

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
    public BigDecimal calculateConsumption(String tobaccoProduct) {
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
        return getTetsimLegalTobaccoProducts().stream()
                .map(tobaccoProduct -> calculateConsumption(tobaccoProduct))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate Revenue Tobacco levy: Levy per pack AT * consumption in mill packs
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateRevenueTobaccoLevy(String tobaccoProduct) {
        return calculateConsumption(tobaccoProduct).multiply(calculateTobaccoLevy(tobaccoProduct));
    }


    /**
     * Calculate Revenue Excise tax on domestic production: excise tax per pack AT * consumption in mill packs
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateRevenueExciseTaxDomesticProduction(String tobaccoProduct) {
        return calculateConsumption(tobaccoProduct).multiply(calculateExciseTaxDomesticProduction(tobaccoProduct));
    }

    /**
     * Calculate Revenue Excise tax on imported production: excise tax (imported) per pack AT * consumption
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateRevenueExciseTaxImportedCigarettes(String tobaccoProduct) {
        return calculateConsumption(tobaccoProduct).multiply(calculateExciseTaxImportedCigarettes(tobaccoProduct));
    }

    /**
     * Calculate Revenue Customs Duty on Imported Cigarettes: customs per pack * consumption
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateRevenueCustomsDutyImportedCigarettes(String tobaccoProduct) {
        return calculateConsumption(tobaccoProduct).multiply(calculateCustomsDutyImportedCigarettes(tobaccoProduct));
    }

    /**
     * Calculate Revenue VAT: VAT per pack * consumption
     *
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateRevenueVat(String tobaccoProduct) {
        return calculateConsumption(tobaccoProduct).multiply(calculateVat(tobaccoProduct));
    }

    /**
     * Calculate Total Excise TaxesRevenue
     *
     * @param tobaccoProduct
     */
    public BigDecimal calculateTotalExciseTaxesRevenue(String tobaccoProduct) {
        return calculateRevenueExciseTaxDomesticProduction(tobaccoProduct)
                .add(calculateRevenueExciseTaxImportedCigarettes(tobaccoProduct));
    }

    /**
     * Calculate Total Excise Revenue: sum of excise tax
     */
    public BigDecimal calculateTotalExciseRevenue() {
        return getTetsimLegalTobaccoProducts().stream()
                .map(tobaccoProduct -> calculateTotalExciseTaxesRevenue(tobaccoProduct))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate Total Revenue
     *
     * @param tobaccoProduct
     */
    public BigDecimal calculateTotalRevenue(String tobaccoProduct) {
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
        return getTetsimLegalTobaccoProducts().stream()
                .map(tobaccoProduct -> calculateTotalRevenue(tobaccoProduct))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public BigDecimal calculateBaselineTobaccoLevy(String tobaccoProduct) {
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
    public BigDecimal calculateBaselineExciseTaxOnDomesticProduction(String tobaccoProduct) {
        if (tobaccoProduct.equals("Imported")) {
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
    public BigDecimal calculateBaselineExciseTaxOnImported(String tobaccoProduct) {
        if (tobaccoProduct.equals("Imported")) {
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
    public BigDecimal calculateBaselineCustomsDuty(String tobaccoProduct) {
        if (tobaccoProduct.equals("Imported")) {
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
    public BigDecimal calculateBaselineRetailPrice(String tobaccoProduct) {
        return getTobaccoProductValueFromVariable(dataset.getRetailPrice(), tobaccoProduct);
    }

    /**
     * Calculate baseeline VAT: Baseline Retail Price * VAT rate / (100 + VAT rate)
     * @param tobaccoProduct
     * @return
     */
    public BigDecimal calculateBaselineVAT(String tobaccoProduct) {
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
    public BigDecimal calculateBaselineNetTaxPrice(String tobaccoProduct) {
        BigDecimal baselineRetailPrice = calculateBaselineRetailPrice(tobaccoProduct);

        if (tobaccoProduct.equals("Illicit")) {
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
    public BigDecimal calculateBaselineExciseBurden(String tobaccoProduct) {
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
    public BigDecimal calculateBaselineTotalTaxBurden(String tobaccoProduct) {
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
    public BigDecimal calculateBaselineConsumption(String tobaccoProduct) {
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
        return getTetsimLegalTobaccoProducts().stream()
                .map(tobaccoProduct -> calculateBaselineConsumption(tobaccoProduct))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<String> getTetsimLegalTobaccoProducts() {
        return dataset.getRetailPrice().getValues().stream()
                .filter(pv -> !pv.getProduct().isIllicit())
                .map(pv -> pv.getProduct().getLabel())
                .collect(Collectors.toList());
    }

}
