package org.devgateway.toolkit.forms.wicket.page.edit.admin;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import nl.dries.wicket.hibernate.dozer.DozerModel;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.exceptions.NullListPageClassException;
import org.devgateway.toolkit.forms.service.DatasetClientService;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapCancelButton;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapSubmitButton;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.visitors.GenericBootstrapValidationVisitor;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceDimensionsPage;
import org.devgateway.toolkit.persistence.dto.ServiceDimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu Page used to make editing easy, extend to get easy access
 * to one entity for editing
 */
@MountPath(value = "/editServiceDimension")
public class EditServiceDimensionPage extends BasePage {
    private static final long serialVersionUID = -1594571319284288551L;

    private static final Logger logger = LoggerFactory.getLogger(EditServiceDimensionPage.class);

    /**
     * Factory method for the new instance of the entity being editing. This
     * will be invoked only when the parameter PARAM_ID is null
     *
     * @return
     */
    /**
     * The repository used to fetch and save the entity, this is initialized in
     * subclasses
     */
    /**
     * The page that is responsible for listing the entities (used here as a
     * return reference after successful save)
     */
    protected Class<? extends BasePage> listPageClass;

    @SpringBean
    DatasetClientService datasetClientService;

    /**
     * The form used by all subclasses
     */
    protected EditForm editForm;

    private CompoundPropertyModel<ServiceDimension> compoundModel;

    /**
     * the entity id, or null if a new entity is requested
     */
    protected Long entityId;

    /**
     * This is a wrapper model that ensures we can easily edit the properties of
     * the entity
     */
    /**
     * generic submit button for the form
     */
    protected BootstrapSubmitButton saveButton;

    /**
     * generic delete button for the form
     */
    protected TextContentModal cancelModal;

    protected TextContentModal saveFailedModal;

    public EditForm getEditForm() {
        return editForm;
    }

    public GenericBootstrapValidationVisitor getBootstrapValidationVisitor(final AjaxRequestTarget target) {
        return new GenericBootstrapValidationVisitor(target);
    }

    protected TextContentModal createCancelModal() {
        final TextContentModal cancelModal = new TextContentModal("cancelModal",
                Model.of("Are you sure you want to cancel? Any changes made will be lost."));

        final LaddaAjaxButton cancelButton = new BootstrapCancelButton("button", Model.of("Yes")) {
            private static final long serialVersionUID = -9144254663723097155L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                cancelModal.appendCloseDialogJavaScript(target);
                setResponsePage(listPageClass, getCancelPageParameters());
            }
        };

        cancelButton.setType(Buttons.Type.Success);
        cancelModal.addButton(cancelButton);
        cancelModal.addCloseButton(Model.of("No"));

        return cancelModal;
    }

    protected TextContentModal createSaveFailedModal() {
        final TextContentModal modal = new TextContentModal("saveFailedModal", Model.of("Save failed!"));
        modal.header(new ResourceModel("error"));
        final LaddaAjaxButton okButton = new LaddaAjaxButton("button", Buttons.Type.Info) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                setResponsePage(listPageClass);
            }
        };
        okButton.setDefaultFormProcessing(false);
        okButton.setLabel(Model.of("OK"));
        modal.addButton(okButton);

        modal.add(new AjaxEventBehavior("hidden.bs.modal") {
            @Override
            protected void onEvent(final AjaxRequestTarget target) {
                setResponsePage(listPageClass);
            }
        });

        return modal;
    }


    public class EditForm extends BootstrapForm<ServiceDimension> {
        private static final long serialVersionUID = -9127043819229346784L;

        /**
         * wrap the model with a {@link CompoundPropertyModel} to ease editing
         * of fields
         *
         * @param model
         */
        public void setCompoundPropertyModel(final IModel<ServiceDimension> model) {
            compoundModel = new CompoundPropertyModel<ServiceDimension>(model);
            setModel(compoundModel);
        }

        public EditForm(final String id, final IModel<ServiceDimension> model) {
            this(id);
            setCompoundPropertyModel(model);
        }

        public EditForm(final String id) {
            super(id);

            setOutputMarkupId(true);

            saveButton = getSaveEditPageButton();
            add(saveButton);

            cancelModal = createCancelModal();
            add(cancelModal);

            saveFailedModal = createSaveFailedModal();
            add(saveFailedModal);

            add(getCancelButton("cancel"));
        }
    }

    protected BootstrapCancelButton getCancelButton(final String id) {
        return new BootstrapCancelButton(id, new StringResourceModel("cancelButton", this, null)) {
            private static final long serialVersionUID = -249084359200507749L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                setResponsePage(listPageClass, getCancelPageParameters());
            }
        };
    }

    /**
     * Generic functionality for the save page button, this can be extended
     * further by subclasses
     *
     */
    public class SaveEditPageButton extends BootstrapSubmitButton {
        private static final long serialVersionUID = 9075809391795974349L;

        private boolean redirect = true;

        private boolean redirectToSelf = false;

        public SaveEditPageButton(final String id, final IModel<String> model) {
            super(id, model);
        }

        @Override
        protected void onSubmit(final AjaxRequestTarget target) {
            try {
                // save the object and go back to the list page
                ServiceDimension dimension = editForm.getModelObject();

                if (editForm.getModelObject().getId() != null) {
                    updateDimension(dimension);
                } else {
                    createDimension(dimension);
                }

                // only redirect if redirect is true
                if (redirectToSelf) {
                    // we need to close the blockUI if it's opened and enable all
                    // the buttons
                    target.appendJavaScript("$.unblockUI();");
                    target.appendJavaScript("$('#" + editForm.getMarkupId() + " button').prop('disabled', false);");
                } else if (redirect) {
                    setResponsePage(getResponsePage(), getParameterPage());
                }

                // redirect is set back to true, which is the default behavior
                redirect = true;
                redirectToSelf = false;
            } catch (ObjectOptimisticLockingFailureException e) {
                saveFailedModal.show(target);
            }
        }

        /**
         * by default, submit button returns back to listPage
         *
         * @return
         */
        protected Class<? extends Page> getResponsePage() {
            return listPageClass;
        }

        /**
         * no params by default
         *
         * @return
         */
        protected PageParameters getParameterPage() {
            return getSaveEditParameters();
        }

        @Override
        protected void onError(final AjaxRequestTarget target) {
            // make all errors visible
            GenericBootstrapValidationVisitor genericBootstrapValidationVisitor = getBootstrapValidationVisitor(target);
            editForm.visitChildren(GenericBootstrapFormComponent.class, genericBootstrapValidationVisitor);

            ValidationError error = new ValidationError();
            error.addKey("formHasErrors");
            error(error);

            target.add(feedbackPanel);

            // autoscroll down to the feedback panel
            target.appendJavaScript("$('html, body').animate({scrollTop: $(\".feedbackPanel\").offset().top}, 500);");
        }

        /**
         * @return the redirect
         */
        public boolean isRedirect() {
            return redirect;
        }

        /**
         * @param redirect the redirect to set
         */
        public void setRedirect(final boolean redirect) {
            this.redirect = redirect;
        }

        /**
         * @param redirectToSelf the redirectToSelf to set
         */
        public void setRedirectToSelf(final boolean redirectToSelf) {
            this.redirectToSelf = redirectToSelf;
        }

        /**
         * @return the redirectToSelf
         */
        public boolean isRedirectToSelf() {
            return redirectToSelf;
        }
    }

    protected PageParameters getSaveEditParameters() {
        return getParamsWithServiceInformation();
    }

    protected PageParameters getCancelPageParameters() {
        return getParamsWithServiceInformation();
    }

    /**
     * Override this to create new save buttons with additional behaviors
     *
     * @return
     */
    protected SaveEditPageButton getSaveEditPageButton() {
        return new SaveEditPageButton("save", new StringResourceModel("saveButton", this, null));
    }

    public EditServiceDimensionPage(final PageParameters parameters) {
        super(parameters);

        if (!parameters.get(WebConstants.PARAM_ID).isNull()) {
            entityId = parameters.get(WebConstants.PARAM_ID).toLongObject();
        }

        editForm = new EditForm("editForm");

        // use this in order to avoid "ServletRequest does not contain multipart
        // content" error
        // this error appears when we have a file upload component that is
        // hidden or not present in the page when the form is created
        editForm.setMultiPart(true);

        add(editForm);

        // section in child
        pageTitle.setDefaultModel(getTitleModel());

        this.listPageClass = ListServiceDimensionsPage.class;

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // we dont like receiving null list pages
        if (listPageClass == null) {
            throw new NullListPageClassException();
        }

        IModel<ServiceDimension> model = null;
        String service = getPageParameters().get(WebConstants.PARAM_SERVICE).toString();

        if (entityId != null) {
            ServiceDimension serviceDimension = datasetClientService.getDimensionById(service, entityId);
            model = new DozerModel<>(serviceDimension);
        } else {
            ServiceDimension serviceDimension = new ServiceDimension();
            if (serviceDimension != null) {
                model = new DozerModel<>(serviceDimension);
            }
        }

        if (model != null) {
            editForm.setCompoundPropertyModel(model);
        }

        final TextFieldBootstrapFormComponent<String> code = new TextFieldBootstrapFormComponent<>("code");
        code.setEnabled(false);
        editForm.add(code);

        final TextFieldBootstrapFormComponent<String> value = new TextFieldBootstrapFormComponent<>("value");
        value.getField().setRequired(true);
        editForm.add(value);

        final TextFieldBootstrapFormComponent<Integer> position = new TextFieldBootstrapFormComponent<>("position");
        position.getField().setRequired(true);
        editForm.add(position);
    }

    protected StringResourceModel getTitleModel() {
        if (entityId == null) {
            return new StringResourceModel("page.title.add", this, null);
        } else {
            return new StringResourceModel("page.title.edit", this, null);
        }
    }

    protected PageParameters getParamsWithServiceInformation() {
        String service = getPageParameters().get(WebConstants.PARAM_SERVICE).toString();

        PageParameters pageParams = new PageParameters();
        pageParams.add(WebConstants.PARAM_SERVICE, service);

        return pageParams;
    }

    private void createDimension(ServiceDimension serviceDimension) {
        String service = getPageParameters().get(WebConstants.PARAM_SERVICE).toString();
        datasetClientService.addDimensionToService(service, serviceDimension);
    }

    private void updateDimension(ServiceDimension serviceDimension) {
        String service = getPageParameters().get(WebConstants.PARAM_SERVICE).toString();
        datasetClientService.updateDimension(service, serviceDimension);
    }

}
