package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.client.DataSetClientException;
import org.devgateway.toolkit.forms.service.DatasetClientService;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapCancelButton;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditStatusEntityPage;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListCSVDatasetPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.service.category.TobaccoProductService;
import org.devgateway.toolkit.persistence.service.data.CSVDatasetService;
import org.devgateway.toolkit.web.util.SettingsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.wicketstuff.annotation.mount.MountPath;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DELETED;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.PUBLISHING;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.SAVED;

/**
 * @author vchihai
 */
@MountPath(value = "/editCSVDataset")
public class EditCSVDatasetPage extends AbstractEditStatusEntityPage<CSVDataset> {

    private static final long serialVersionUID = -5231470856974604314L;

    private static final Logger logger = LoggerFactory.getLogger(EditCSVDatasetPage.class);

    protected Select2ChoiceBootstrapFormComponent<Integer> year;

    protected TextFieldBootstrapFormComponent destinationService;

    @SpringBean
    protected CSVDatasetService csvDatasetService;

    @SpringBean
    protected EurekaClientService eurekaClientService;

    @SpringBean
    protected DatasetClientService datasetClientService;

    @SpringBean
    protected SettingsUtils settingsUtils;

    @SpringBean
    protected TobaccoProductService tobaccoProductService;

    public EditCSVDatasetPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = csvDatasetService;
        this.listPageClass = ListCSVDatasetPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (entityId == null) {
            String service = getPageParameters().get(WebConstants.PARAM_SERVICE).toString();
            editForm.getModelObject().setDestinationService(service);
        }

        editForm.add(getYear());

        final TextFieldBootstrapFormComponent<String> description =
                new TextFieldBootstrapFormComponent<>("description");
        description.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_ONE_LINE_TEXT);
        editForm.add(description);

        final FileInputBootstrapFormComponent files = new FileInputBootstrapFormComponent("files");
        files.allowedFileExtensions("csv");
        files.required();
        files.maxFiles(1);
        files.getFileInputBootstrapFormComponentWrapper().setAllowDownloadWhenReadonly(true);
        editForm.add(files);
        editForm.add(getService());

    }

    private Select2ChoiceBootstrapFormComponent<Integer> getYear() {
        year = new Select2ChoiceBootstrapFormComponent<>("year",
                new GenericChoiceProvider<>(settingsUtils.getYearsRange()));
        editForm.add(year);
        year.required();

        return year;
    }

    private TextFieldBootstrapFormComponent<String> getService() {
        destinationService = new TextFieldBootstrapFormComponent("destinationService");
        destinationService.setEnabled(false);

        return destinationService;
    }

    @Override
    protected void onDelete(final AjaxRequestTarget target) {
        try {
            // save the object and go back to the list page
            CSVDataset saveable = editForm.getModelObject();
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

    @Override
    protected void onAfterRevertToDraft(final AjaxRequestTarget target) {
        try {
            datasetClientService.unpublishDataset(editForm.getModelObject());
        } catch (DataSetClientException | Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    protected void onApprove(final AjaxRequestTarget target) {
        try {
            CSVDataset dataset = editForm.getModelObject();

            FileMetadata fileMetadata = dataset.getFiles().stream().findFirst().get();
            String fileName = fileMetadata.getName();
            byte[] content = fileMetadata.getContent().getBytes();

            datasetClientService.publishDataset(dataset, fileName, content);
            dataset.setStatus(PUBLISHING);
        } catch (DataSetClientException | Exception e) {
            logger.error(e.getMessage(), e);
            approveFailedModal.show(target);
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

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        if (SAVED.equals(editForm.getModelObject().getStatus())) {
            destinationService.setEnabled(true);
        }
    }

    @Override
    protected void enableDisableAutosaveFields(final AjaxRequestTarget target) {
        super.enableDisableAutosaveFields(target);

        if (StringUtils.isBlank(editForm.getModelObject().getDestinationService())) {
            saveApproveButton.setEnabled(false);
            approveButton.setEnabled(false);
        }
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
        pageParams.add(WebConstants.PARAM_SERVICE, editForm.getModelObject().getDestinationService());

        return pageParams;
    }
}
