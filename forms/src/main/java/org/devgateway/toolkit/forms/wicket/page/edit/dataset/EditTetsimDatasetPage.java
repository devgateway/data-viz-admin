package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.validators.PositiveBigDecimalValidator;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListTetsimDatasetPage;
import org.devgateway.toolkit.persistence.dao.data.DatasetStatus;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author vchihai
 */
@MountPath(value = "/editTetsimDataset")
public class EditTetsimDatasetPage extends AbstractEditPage<TetsimDataset> {

    private static final long serialVersionUID = -8460878260874111506L;

    @SpringBean
    protected TetsimDatasetService tetsimDatasetService;

    public EditTetsimDatasetPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tetsimDatasetService;
        this.listPageClass = ListTetsimDatasetPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (editForm.getModelObject().isNew()) {
            editForm.getModelObject().setStatus(DatasetStatus.DRAFT);
        }

        editForm.add(getYear());
        editForm.add(getBaseLineNumbers());
    }

    private TextFieldBootstrapFormComponent<Integer> getYear() {
        final TextFieldBootstrapFormComponent<Integer> year = new TextFieldBootstrapFormComponent<>("year");
        year.required();
        year.integer();
        return year;
    }

    private Component getBaseLineNumbers() {
        RepeatingView baseLineNumbers = new RepeatingView("baseLineNumbers");
        baseLineNumbers.add(getCigaretteConsumptionVariable(baseLineNumbers.newChildId()));
        baseLineNumbers.add(getVatRateVariable(baseLineNumbers.newChildId()));
        baseLineNumbers.add(getCigaretteDeclaredCustomValueVariable(baseLineNumbers.newChildId()));
        baseLineNumbers.add(getAdultPopulationVariable(baseLineNumbers.newChildId()));
        baseLineNumbers.add(getSmokingPrevalenceVariable(baseLineNumbers.newChildId()));

        return baseLineNumbers;
    }

    private Component getCigaretteConsumptionVariable(final String id) {
        TetsimBaselineDecimalVariable variable = new TetsimBaselineDecimalVariable(id,
                new StringResourceModel("cigaretteConsumption.label"),
                new StringResourceModel("cigaretteConsumption.unit"),
                new PropertyModel<>(editForm.getModel(), "cigaretteConsumption"));

        variable.getInputField().getField().add(new PositiveBigDecimalValidator());

        return variable;
    }

    private Component getVatRateVariable(final String id) {
        TetsimBaselineDecimalVariable variable = new TetsimBaselineDecimalVariable(id,
                new StringResourceModel("vatRate.label"),
                new StringResourceModel("vatRate.unit"),
                new PropertyModel<>(editForm.getModel(), "vatRate"));

        variable.getInputField().getField().add(new PositiveBigDecimalValidator());

        return variable;
    }

    private Component getCigaretteDeclaredCustomValueVariable(final String id) {
        TetsimBaselineDecimalVariable variable = new TetsimBaselineDecimalVariable(id,
                new StringResourceModel("cigaretteDeclaredCustomValue.label"),
                new StringResourceModel("cigaretteDeclaredCustomValue.unit"),
                new PropertyModel<>(editForm.getModel(), "cigaretteDeclaredCustomValue"));
        variable.getInputField().getField().add(new PositiveBigDecimalValidator(true));

        return variable;
    }

    private Component getAdultPopulationVariable(final String id) {
        TetsimBaselineDecimalVariable variable = new TetsimBaselineDecimalVariable(id,
                new StringResourceModel("adultPopulation.label"),
                new StringResourceModel("adultPopulation.unit"),
                new PropertyModel<>(editForm.getModel(), "adultPopulation"));

        variable.getInputField().getField().add(new PositiveBigDecimalValidator());

        return variable;
    }

    private Component getSmokingPrevalenceVariable(final String id) {
        TetsimBaselineDecimalVariable variable = new TetsimBaselineDecimalVariable(id,
                new StringResourceModel("smokingPrevalence.label"),
                new StringResourceModel("smokingPrevalence.unit"),
                new PropertyModel<>(editForm.getModel(), "smokingPrevalence"));

        variable.getInputField().getField().add(new PositiveBigDecimalValidator());

        return variable;
    }


}
