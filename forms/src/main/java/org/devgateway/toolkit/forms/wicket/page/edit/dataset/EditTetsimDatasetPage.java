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
import org.devgateway.toolkit.forms.client.DataSetClientException;
import org.devgateway.toolkit.forms.service.DatasetClientService;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.forms.wicket.components.breadcrumbs.BreadCrumbPage;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapCancelButton;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditStatusEntityPage;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListTetsimDatasetPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dao.data.TetsimPriceVariable;
import org.devgateway.toolkit.persistence.dao.data.TetsimTobaccoProductValue;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.devgateway.toolkit.persistence.service.tetsim.TetsimOutputService;
import org.devgateway.toolkit.web.util.SettingsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.wicketstuff.annotation.mount.MountPath;

import java.math.BigDecimal;

import static org.devgateway.toolkit.forms.WebConstants.MAXIMUM_PERCENTAGE;
import static org.devgateway.toolkit.forms.WebConstants.PARAM_SERVICE;
import static org.devgateway.toolkit.forms.WebConstants.PARAM_YEAR;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DELETED;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.PUBLISHING;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.UNPUBLISHING;

/**
 * @author vchihai
 */
@MountPath(value = "/editTetsimDataset")
@BreadCrumbPage(parent = ListTetsimDatasetPage.class, hasServiceParam = true)
public class EditTetsimDatasetPage extends AbstractEditStatusEntityPage<TetsimDataset> {

    private static final long serialVersionUID = -8460878260874111506L;

    private static final Logger logger = LoggerFactory.getLogger(EditTetsimDatasetPage.class);

    protected Select2ChoiceBootstrapFormComponent<Integer> year;

    protected TextFieldBootstrapFormComponent destinationService;

    @SpringBean
    protected TetsimDatasetService tetsimDatasetService;

    @SpringBean
    protected TetsimOutputService tetsimOutputService;

    @SpringBean
    protected EurekaClientService eurekaClientService;

    @SpringBean
    protected DatasetClientService datasetClientService;

    @SpringBean
    protected SettingsUtils settingsUtils;

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
        editForm.add(getService());

        editForm.add(new TetsimMarketSharePercentageValidator());

        if (editForm.getModelObject().getDestinationService() == null) {
            String service = getPageParameters().get(PARAM_SERVICE).toString();
            editForm.getModelObject().setDestinationService(service);
        }

        if (editForm.getModelObject().isNew() && getYearParam() != null) {
            editForm.getModelObject().setYear(getYearParam());
        }
    }

    private Select2ChoiceBootstrapFormComponent<Integer> getYear() {
        year = new Select2ChoiceBootstrapFormComponent<>("year",
                new GenericChoiceProvider<>(settingsUtils.getYearsRange()));
        editForm.add(year);
        year.required();
        year.setEnabled(false);

        return year;
    }

    private TextFieldBootstrapFormComponent getService() {
        destinationService = new TextFieldBootstrapFormComponent<>("destinationService");
        destinationService.setEnabled(false);
        return destinationService;
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
        setResponsePage(listPageClass, getParamsWithServiceInformation());
    }

    @Override
    protected void onAfterRevertToDraft(final AjaxRequestTarget target) {
        try {
            datasetClientService.unpublishDataset(editForm.getModelObject());
            editForm.getModelObject().setStatus(UNPUBLISHING);
        } catch (DataSetClientException | Exception e) {
            logger.error(e.getMessage(), e);
        }
        setResponsePage(listPageClass);
    }

    protected void onApprove(final AjaxRequestTarget target) {
        try {
            TetsimDataset dataset = editForm.getModelObject();
            String fileName = dataset.getYear() + "_tetsim.csv";
            byte[] content = tetsimOutputService.getTetsimCSVDatasetOutputs(dataset.getId());

            datasetClientService.publishDataset(dataset, fileName, content);

            dataset.setStatus(PUBLISHING);
        } catch (DataSetClientException | Exception e) {
            logger.error(e.getMessage(), e);
            failedModal.show(target);
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
        return new TetsimPriceAnalysisPanel("priceAnalysisNumbers", editForm.getModel());
    }

    private Component getIndustryResponsesNumbers() {
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

    @Override
    protected void enableDisableAutosaveFields(final AjaxRequestTarget target) {
        super.enableDisableAutosaveFields(target);
    }


    protected Integer getYearParam() {
        return getPageParameters().get(PARAM_YEAR).toOptionalInteger();
    }

    @Override
    protected PageParameters getSaveEditParameters() {
        return getParamsWithServiceInformation();
    }

    @Override
    protected PageParameters getCancelPageParameters() {
        return getParamsWithServiceInformation();
    }


    protected PageParameters getParamsWithServiceInformation() {
        PageParameters pageParams = new PageParameters();
        // add service to the page parameters
        pageParams.add(PARAM_SERVICE, editForm.getModelObject().getDestinationService());

        return pageParams;
    }

    protected IModel<String> getBreadcrumbTitleModel() {
        return getTitleModel();
    }

}
