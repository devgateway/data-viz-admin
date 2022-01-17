package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.validators.PositiveBigDecimalValidator;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.persistence.dao.categories.TobaccoProduct;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;
import org.devgateway.toolkit.persistence.service.category.TobaccoProductService;

import java.math.BigDecimal;
import java.util.List;

import static org.devgateway.toolkit.forms.WebConstants.MAXIMUM_PERCENTAGE;

/**
 * @author Viorel Chihai
 */
public class TetsimPriceAnalysisPanel extends Panel {

    protected final IModel<TetsimDataset> tetsimDatasetIModel;

    @SpringBean
    protected TobaccoProductService tobaccoProductService;

    @SpringBean
    protected AdminSettingsService adminSettingsService;

    public TetsimPriceAnalysisPanel(final String id, final IModel<TetsimDataset> tetsimDatasetIModel) {
        super(id);
        this.tetsimDatasetIModel = tetsimDatasetIModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(getPriceAnalysisHeaders());
        add(getPriceAnalysisVariables());
    }

    private Component getPriceAnalysisHeaders() {
        RepeatingView analysisHeaders = new RepeatingView("analysisHeaders");
        analysisHeaders.add(new Label(analysisHeaders.newChildId(), Model.of("Price Variable")));
        analysisHeaders.add(new Label(analysisHeaders.newChildId(), Model.of("Unit")));

        List<TobaccoProduct> tobaccoProducts = tobaccoProductService.findAllSorted();
        for (TobaccoProduct tobaccoProduct : tobaccoProducts) {
            analysisHeaders.add(new Label(analysisHeaders.newChildId(),
                    new PropertyModel<>(tobaccoProduct, "label")));
        }

        return analysisHeaders;
    }

    private RepeatingView getPriceAnalysisVariables() {
        RepeatingView variables = new RepeatingView("analysisVariables");

        variables.add(getRetailPriceVariable(variables.newChildId()));
        variables.add(getMarketShareVariable(variables.newChildId()));
        variables.add(getCifVariable(variables.newChildId()));
        variables.add(getTobaccoLevy(variables.newChildId()));
        variables.add(getExciseTax(variables.newChildId()));
        variables.add(getCustomsDuty(variables.newChildId()));
        variables.add(getElasticityOfDemandPrice(variables.newChildId()));
        variables.add(getElasticityOfDemandCrossPrice(variables.newChildId()));
        variables.add(getChangeInIllicitNot(variables.newChildId()));

        return variables;
    }

    /**
     * Get retail price variable panel with tobacco product inputs.
     * Mandatory. Numerical fields with decimals. No negative or 0 values accepted.
     *
     * @param id
     * @return TetsimTobaccoProductsVariable
     */
    private TetsimTobaccoProductsVariable getRetailPriceVariable(final String id) {
        return new TetsimTobaccoProductsVariable(id,
                new StringResourceModel("retailPrice.label"),
                new StringResourceModel("retailPrice.unit").setParameters(getDefaultCurrency()),
                new PropertyModel<>(tetsimDatasetIModel, "retailPrice")) {

            @Override
            protected void addBehavioursToTobaccoProductVariable(final TextFieldBootstrapFormComponent variable1) {
                super.addBehavioursToTobaccoProductVariable(variable1);
                variable1.getField().add(new PositiveBigDecimalValidator());
            }
        };
    }

    /**
     * Get market share variable panel with tobacco product inputs.
     * Mandatory. Numerical fields with decimals. No negative values accepted.
     * The total of all inputs should add up to 100.
     *
     * @param id
     * @return TetsimTobaccoProductsVariable
     */
    private TetsimTobaccoProductsVariable getMarketShareVariable(final String id) {
        return new TetsimTobaccoProductsVariable(id,
                new StringResourceModel("marketShare.label"),
                new StringResourceModel("marketShare.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "marketShare")) {

            @Override
            protected void addBehavioursToTobaccoProductVariable(final TextFieldBootstrapFormComponent variable1) {
                super.addBehavioursToTobaccoProductVariable(variable1);
                variable1.getField().add(new PositiveBigDecimalValidator(true));
                variable1.getField().add(RangeValidator.maximum(BigDecimal.valueOf(MAXIMUM_PERCENTAGE)));
            }
        };
    }

    /**
     * Get CIF variable panel with tobacco product inputs.
     * Mandatory. Numerical fields with decimals. No negative values accepted.
     *
     * @param id
     * @return TetsimTobaccoProductsVariable
     */
    private TetsimTobaccoProductsVariable getCifVariable(final String id) {
        return new TetsimTobaccoProductsVariable(id,
                new StringResourceModel("cif.label"),
                new StringResourceModel("cif.unit").setParameters(getDefaultCurrency()),
                new PropertyModel<>(tetsimDatasetIModel, "cif")) {

            @Override
            protected void addBehavioursToTobaccoProductVariable(final TextFieldBootstrapFormComponent variable1) {
                super.addBehavioursToTobaccoProductVariable(variable1);
                variable1.getField().add(new PositiveBigDecimalValidator(true));
            }
        };
    }

    /**
     * Get tobacco levy variable panel with tobacco product inputs.
     * Mandatory. Numerical fields with decimals. No negative values accepted.
     *
     * @param id
     * @return TetsimTobaccoProductsVariable
     */
    private TetsimTobaccoProductsVariable getTobaccoLevy(final String id) {
        return new TetsimTobaccoProductsVariable(id,
                new StringResourceModel("tobaccoLevy.label"),
                new StringResourceModel("tobaccoLevy.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "tobaccoLevy")) {

            @Override
            protected void addBehavioursToTobaccoProductVariable(final TextFieldBootstrapFormComponent variable1) {
                super.addBehavioursToTobaccoProductVariable(variable1);
                variable1.getField().add(new PositiveBigDecimalValidator(true));
                variable1.getField().add(RangeValidator.maximum(BigDecimal.valueOf(MAXIMUM_PERCENTAGE)));
            }
        };
    }

    /**
     * Get excise tax variable panel with tobacco product inputs.
     * Mandatory. Numerical fields with decimals. No negative values accepted.
     *
     * @param id
     * @return TetsimTobaccoProductsVariable
     */
    private TetsimTobaccoProductsVariable getExciseTax(final String id) {
        return new TetsimTobaccoProductsVariable(id,
                new StringResourceModel("exciseTax.label"),
                new StringResourceModel("exciseTax.unit").setParameters(getDefaultCurrency()),
                new PropertyModel<>(tetsimDatasetIModel, "exciseTax")) {

            @Override
            protected void addBehavioursToTobaccoProductVariable(final TextFieldBootstrapFormComponent variable1) {
                super.addBehavioursToTobaccoProductVariable(variable1);
                variable1.getField().add(new PositiveBigDecimalValidator(true));
            }
        };
    }

    /**
     * Get customs duty variable panel with tobacco product inputs.
     * Not Mandatory. Numerical fields with decimals. No negative values accepted.
     *
     * @param id
     * @return TetsimTobaccoProductsVariable
     */
    private TetsimTobaccoProductsVariable getCustomsDuty(final String id) {
        return new TetsimTobaccoProductsVariable(id,
                new StringResourceModel("customsDuty.label"),
                new StringResourceModel("customsDuty.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "customsDuty"), false) {

            @Override
            protected void addBehavioursToTobaccoProductVariable(final TextFieldBootstrapFormComponent variable1) {
                super.addBehavioursToTobaccoProductVariable(variable1);
                variable1.getField().add(new PositiveBigDecimalValidator(true));
            }
        };
    }

    /**
     * Get price elasticity of demand variable panel with tobacco product inputs.
     * Mandatory. Numerical fields with decimals between -1 and 1.
     *
     * @param id
     * @return TetsimTobaccoProductsVariable
     */
    private TetsimTobaccoProductsVariable getElasticityOfDemandPrice(final String id) {
        return new TetsimTobaccoProductsVariable(id,
                new StringResourceModel("elasticityOfDemandPrice.label"),
                new StringResourceModel("elasticityOfDemandPrice.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "elasticityOfDemandPrice")) {

            @Override
            protected void addBehavioursToTobaccoProductVariable(final TextFieldBootstrapFormComponent variable1) {
                super.addBehavioursToTobaccoProductVariable(variable1);
                variable1.getField().add(RangeValidator.range(BigDecimal.valueOf(-1), BigDecimal.ONE));
            }
        };
    }

    /**
     * Get cross-price elasticity of demand variable panel with tobacco product inputs.
     * Mandatory. Numerical fields with decimals between -1 and 1.
     *
     * @param id
     * @return TetsimTobaccoProductsVariable
     */
    private TetsimTobaccoProductsVariable getElasticityOfDemandCrossPrice(final String id) {
        return new TetsimTobaccoProductsVariable(id,
                new StringResourceModel("elasticityOfDemandCrossPrice.label"),
                new StringResourceModel("elasticityOfDemandCrossPrice.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "elasticityOfDemandPrice")) {

            @Override
            protected void addBehavioursToTobaccoProductVariable(final TextFieldBootstrapFormComponent variable1) {
                super.addBehavioursToTobaccoProductVariable(variable1);
                variable1.getField().add(RangeValidator.range(BigDecimal.valueOf(-1), BigDecimal.ONE));
            }
        };
    }

    /**
     * Get change in illicit not variable panel with tobacco product inputs.
     * Only allowed for product marked as illicit in the system category.
     * Numerical field with decimals.
     *
     * @param id
     * @return TetsimTobaccoProductsVariable
     */
    private TetsimTobaccoProductsVariable getChangeInIllicitNot(final String id) {
        return new TetsimTobaccoProductsVariable(id,
                new StringResourceModel("changeInIllicitNot.label"),
                new StringResourceModel("changeInIllicitNot.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "changeInIllicitNot"), false, true) {
        };
    }

    private String getDefaultCurrency() {
        return adminSettingsService.get().getDefaultCurrency().getLabel();
    }

}


