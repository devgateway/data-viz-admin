package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.toolkit.forms.validators.PositiveBigDecimalValidator;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;

/**
 * @author Viorel Chihai
 */
public class TetsimBaselineNumbersPanel extends Panel {

    protected final IModel<TetsimDataset> tetsimDatasetIModel;

    public TetsimBaselineNumbersPanel(final String id, final IModel<TetsimDataset> tetsimDatasetIModel) {
        super(id);
        this.tetsimDatasetIModel = tetsimDatasetIModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(getBaseLineNumbers());
    }

    private RepeatingView getBaseLineNumbers() {
        RepeatingView baseLineNumbers = new RepeatingView("numbers");
        baseLineNumbers.add(getCigaretteConsumptionVariable(baseLineNumbers.newChildId()));
        baseLineNumbers.add(getVatRateVariable(baseLineNumbers.newChildId()));
        baseLineNumbers.add(getCigaretteDeclaredCustomValueVariable(baseLineNumbers.newChildId()));
        baseLineNumbers.add(getAdultPopulationVariable(baseLineNumbers.newChildId()));
        baseLineNumbers.add(getSmokingPrevalenceVariable(baseLineNumbers.newChildId()));

        return baseLineNumbers;
    }

    private TetsimBaselineDecimalVariable getCigaretteConsumptionVariable(final String id) {
        TetsimBaselineDecimalVariable variable = new TetsimBaselineDecimalVariable(id,
                new StringResourceModel("cigaretteConsumption.label"),
                new StringResourceModel("cigaretteConsumption.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "cigaretteConsumption"));

        variable.getInputField().getField().add(new PositiveBigDecimalValidator());

        return variable;
    }

    private TetsimBaselineDecimalVariable getVatRateVariable(final String id) {
        TetsimBaselineDecimalVariable variable = new TetsimBaselineDecimalVariable(id,
                new StringResourceModel("vatRate.label"),
                new StringResourceModel("vatRate.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "vatRate"));

        variable.getInputField().getField().add(new PositiveBigDecimalValidator());

        return variable;
    }

    private TetsimBaselineDecimalVariable getCigaretteDeclaredCustomValueVariable(final String id) {
        TetsimBaselineDecimalVariable variable = new TetsimBaselineDecimalVariable(id,
                new StringResourceModel("cigaretteDeclaredCustomValue.label"),
                new StringResourceModel("cigaretteDeclaredCustomValue.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "cigaretteDeclaredCustomValue"));
        variable.getInputField().getField().add(new PositiveBigDecimalValidator(true));

        return variable;
    }

    private TetsimBaselineDecimalVariable getAdultPopulationVariable(final String id) {
        TetsimBaselineDecimalVariable variable = new TetsimBaselineDecimalVariable(id,
                new StringResourceModel("adultPopulation.label"),
                new StringResourceModel("adultPopulation.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "adultPopulation"));

        variable.getInputField().getField().add(new PositiveBigDecimalValidator());

        return variable;
    }

    private TetsimBaselineDecimalVariable getSmokingPrevalenceVariable(final String id) {
        TetsimBaselineDecimalVariable variable = new TetsimBaselineDecimalVariable(id,
                new StringResourceModel("smokingPrevalence.label"),
                new StringResourceModel("smokingPrevalence.unit"),
                new PropertyModel<>(tetsimDatasetIModel, "smokingPrevalence"));

        variable.getInputField().getField().add(new PositiveBigDecimalValidator());

        return variable;
    }
}


