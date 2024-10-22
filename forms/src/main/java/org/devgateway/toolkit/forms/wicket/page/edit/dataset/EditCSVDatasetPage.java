package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.client.DataSetClientException;
import org.devgateway.toolkit.forms.service.DatasetClientService;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.forms.wicket.components.breadcrumbs.BreadCrumbPage;
import org.devgateway.toolkit.forms.wicket.components.form.AJAXDownload;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapCancelButton;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditStatusEntityPage;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListCSVDatasetPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.service.data.CSVDatasetService;
import org.devgateway.toolkit.web.util.SettingsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.wicketstuff.annotation.mount.MountPath;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DELETED;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.PUBLISHING;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.UNPUBLISHING;

/**
 * @author vchihai
 */
@MountPath(value = "/editCSVDataset")
@BreadCrumbPage(parent = ListCSVDatasetPage.class, hasServiceParam = true)
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

        AJAXDownload downloadTemplateBehaviour = getDownloadTemplateBehaviour();
        editForm.add(downloadTemplateBehaviour);
        editForm.add(getDownloadTemplateButton(downloadTemplateBehaviour));

    }

    private AJAXDownload getDownloadTemplateBehaviour() {
        final AJAXDownload download = new AJAXDownload() {
            @Override
            protected IRequestHandler getHandler() {
                return new IRequestHandler() {
                    @Override
                    public void respond(final IRequestCycle requestCycle) {
                        final HttpServletResponse response = (HttpServletResponse) requestCycle.getResponse().getContainerResponse();
                        try {
                            String serviceName = editForm.getModelObject().getDestinationService();

                            final byte[] bytes = datasetClientService.getTemplateDownload(serviceName);

                            response.setContentType("text/csv");
                            response.setHeader("Content-Disposition", "attachment; filename=" + serviceName + "-template.csv");
                            response.getOutputStream().write(bytes);
                        } catch (IOException e) {
                            logger.error("Download Template error", e);
                        }

                        RequestCycle.get().scheduleRequestHandlerAfterCurrent(null);
                    }

                    @Override
                    public void detach(final IRequestCycle requestCycle) {
                        // do nothing;
                    }
                };
            }
        };

        return download;
    }

    private BootstrapAjaxButton getDownloadTemplateButton(final AJAXDownload downloadTemplateBehaviour) {
        final LaddaAjaxButton templateDownloadButton = new LaddaAjaxButton("templateDownloadButton",
                new Model<>("Template Download"),
                Buttons.Type.Warning) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);

                // initiate the file download
                downloadTemplateBehaviour.initiate(target);
            }
        };
        templateDownloadButton.setDefaultFormProcessing(false);
        templateDownloadButton.setIconType(FontAwesome5IconType.file_csv_s);
        return templateDownloadButton;
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
        setResponsePage(listPageClass, getSaveEditParameters());
    }

    protected void onAfterRevertToDraft(final AjaxRequestTarget target) {
        try {
            CSVDataset dataset = editForm.getModelObject();
            datasetClientService.unpublishDataset(editForm.getModelObject());
            dataset.setStatus(UNPUBLISHING);
        } catch (DataSetClientException | Exception e) {
            logger.error(e.getMessage(), e);
            failedModal.show(target);
        }
        setResponsePage(listPageClass);
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

    protected IModel<String> getBreadcrumbTitleModel() {
        return getTitleModel();
    }
}
