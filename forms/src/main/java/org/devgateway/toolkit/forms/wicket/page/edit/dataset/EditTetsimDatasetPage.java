package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.validators.UniquePropertyValidator;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapCancelButton;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditStatusEntityPage;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListTetsimDatasetPage;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dao.data.TetsimPriceVariable;
import org.devgateway.toolkit.persistence.dao.data.TetsimTobaccoProductValue;
import org.devgateway.toolkit.persistence.service.category.TobaccoProductService;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.wicketstuff.annotation.mount.MountPath;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.devgateway.toolkit.forms.WebConstants.MAXIMUM_PERCENTAGE;
import static org.devgateway.toolkit.persistence.dao.DBConstants.MAX_YEAR_DATASET;
import static org.devgateway.toolkit.persistence.dao.DBConstants.MIN_YEAR_DATASET;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DELETED;

/**
 * @author vchihai
 */
@MountPath(value = "/editTetsimDataset")
public class EditTetsimDatasetPage extends AbstractEditStatusEntityPage<TetsimDataset> {

    private static final long serialVersionUID = -8460878260874111506L;

    @SpringBean
    protected TetsimDatasetService tetsimDatasetService;

    @SpringBean
    protected TobaccoProductService tobaccoProductService;

    public EditTetsimDatasetPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tetsimDatasetService;
        this.listPageClass = ListTetsimDatasetPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        editForm.add(getYear());
        editForm.add(getBaseLineNumbers());
        editForm.add(getPriceAnalysisNumbers());
        editForm.add(getIndustryResponsesNumbers());

        editForm.add(new TetsimMarketSharePercentageValidator());
    }

    private TextFieldBootstrapFormComponent<Integer> getYear() {
        final TextFieldBootstrapFormComponent<Integer> year = new TextFieldBootstrapFormComponent<>("year");
        year.required();
        year.integer();
        year.getField().add(RangeValidator.range(MIN_YEAR_DATASET, MAX_YEAR_DATASET));

        final StringValue id = getPageParameters().get(WebConstants.PARAM_ID);
        List<Long> deletedIds = tetsimDatasetService.findAllDeleted().stream()
                .map(TetsimDataset::getId)
                .collect(Collectors.toList());
        deletedIds.add(id.toLong(-1L));
        year.getField().add(new UniquePropertyValidator<>(tetsimDatasetService, deletedIds,"year", this));

        return year;
    }

    @Override
    protected void onDelete(final AjaxRequestTarget target) {
        try {
            // save the object and go back to the list page
            TetsimDataset saveable = editForm.getModelObject();
            saveable.setStatus(DELETED);

            beforeSaveEntity(saveable);
            // saves the entity and flushes the changes
            jpaService.saveAndFlush(saveable);

            // clears session and detaches all entities that are currently attached
            entityManager.clear();

            // we flush the mondrian/wicket/reports cache to ensure it gets rebuilt
            flushReportingCaches();
            afterSaveEntity(saveable);
        } catch (ObjectOptimisticLockingFailureException e) {
            deleteFailedModal.show(target);
            target.add(deleteFailedModal);
        }
        setResponsePage(listPageClass);
    }

    protected BootstrapCancelButton getCancelButton(final String id) {
        return new CancelEditPageButton(id, new StringResourceModel("cancelButton", this, null));
    }

    public class CancelEditPageButton extends BootstrapCancelButton {
        private static final long serialVersionUID = -1474498211555760931L;

        public CancelEditPageButton(final String id, final IModel<String> model) {
            super(id, model);
        }

        @Override
        protected void onSubmit(final AjaxRequestTarget target) {
            cancelModal.show(true);
            target.add(cancelModal);
        }
    }

    private TetsimBaselineNumbersPanel getBaseLineNumbers() {
        return new TetsimBaselineNumbersPanel("baseLineNumbers", editForm.getModel());
    }

    private Component getPriceAnalysisNumbers() {
        if (tobaccoProductsNotDefined()) {
            return new NoTobaccoProductsPanel("priceAnalysisNumbers");
        }

        return new TetsimPriceAnalysisPanel("priceAnalysisNumbers", editForm.getModel());
    }

    private Component getIndustryResponsesNumbers() {
        if (tobaccoProductsNotDefined()) {
            return new NoTobaccoProductsPanel("industryResponsesNumbers");
        }

        return new TetsimIndustryResponsesPanel("industryResponsesNumbers", editForm.getModel());
    }

    private class TetsimMarketSharePercentageValidator extends AbstractFormValidator {

        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[0];
        }

        @Override
        public void validate(final Form<?> form) {
            TetsimPriceVariable marketShare = editForm.getModelObject().getMarketShare();
            BigDecimal sum = marketShare.getValues().stream()
                    .map(TetsimTobaccoProductValue::getValue)
                    .filter(v -> v != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (sum.intValue() > MAXIMUM_PERCENTAGE) {
                editForm.error(getString("error.form.validation.marketShare.percentage"));
            }
        }
    }

    private boolean tobaccoProductsNotDefined() {
        return tobaccoProductService.count() == 0;
    }

    @Override
    protected void enableDisableAutosaveFields(final AjaxRequestTarget target) {
        super.enableDisableAutosaveFields(target);

        if (tobaccoProductsNotDefined()) {
            saveDraftContinueButton.setEnabled(false);
            saveButton.setEnabled(false);
            submitAndNext.setEnabled(false);
            saveSubmitButton.setEnabled(false);
            saveApproveButton.setEnabled(false);
            approveButton.setEnabled(false);
            revertToDraftPageButton.setEnabled(false);
        }
    }
}
