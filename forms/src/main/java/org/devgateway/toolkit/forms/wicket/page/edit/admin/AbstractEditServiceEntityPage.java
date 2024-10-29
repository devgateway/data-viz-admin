package org.devgateway.toolkit.forms.wicket.page.edit.admin;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import org.devgateway.toolkit.forms.wicket.components.buttons.ladda.LaddaAjaxButton;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
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
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.forms.service.admin.BaseServiceEntityService;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapCancelButton;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapSubmitButton;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.visitors.GenericBootstrapValidationVisitor;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.persistence.dto.ServiceEntity;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.text.MessageFormat;

import static org.devgateway.toolkit.forms.WebConstants.PARAM_SERVICE;

public abstract class AbstractEditServiceEntityPage<T extends ServiceEntity> extends BasePage {
    private static final long serialVersionUID = -2184956023986944919L;

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
    protected Class<? extends BasePage> listPageClass;

    /**
     * The form used by all subclasses
     */
    protected EditForm editForm;

    protected BaseServiceEntityService<T> entityService;

    private CompoundPropertyModel<T> compoundModel;

    /**
     * the entity id, or null if a new entity is requested
     */
    protected Long entityId;

    protected String serviceName;

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

    @SpringBean
    protected EurekaClientService eurekaClientService;

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

    public class EditForm extends BootstrapForm<T> {
        private static final long serialVersionUID = -9127043819229346784L;

        /**
         * wrap the model with a {@link CompoundPropertyModel} to ease editing
         * of fields
         *
         * @param model
         */
        public void setCompoundPropertyModel(final IModel<T> model) {
            compoundModel = new CompoundPropertyModel<T>(model);
            setModel(compoundModel);
        }

        public EditForm(final String id, final IModel<T> model) {
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
                T entity = editForm.getModelObject();

                if (editForm.getModelObject().getId() != null) {
                    entityService.update(serviceName, entity);
                } else {
                    entityService.save(serviceName, entity);
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

    public AbstractEditServiceEntityPage(final PageParameters parameters) {
        super(parameters);

        if (!parameters.get(WebConstants.PARAM_ID).isNull()) {
            entityId = parameters.get(WebConstants.PARAM_ID).toLongObject();
        }

        editForm = new EditForm("editForm");
        editForm.setMultiPart(true);

        add(editForm);

        // section in child
        pageTitle.setDefaultModel(getTitleModel());

        serviceName = getPageParameters().get(PARAM_SERVICE).toString();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // we dont like receiving null list pages
        if (listPageClass == null) {
            throw new NullListPageClassException();
        }

        IModel<T> model = null;

        if (entityId != null) {
            T entity = entityService.findOne(serviceName, entityId);
            model = Model.of(entity);
        } else {
            T entity = entityService.newInstance();
            if (entity != null) {
                model = Model.of(entity);
            }
        }

        if (model != null) {
            editForm.setCompoundPropertyModel(model);
        }

    }

    protected Model<String> getTitleModel() {
        String serviceLabel = getServiceLabel();
        if (entityId == null) {
            return Model.of(MessageFormat.format(getString("page.title.add"), serviceLabel));
        } else {
            return Model.of(MessageFormat.format(getString("page.title.edit"), serviceLabel));
        }
    }

    protected PageParameters getParamsWithServiceInformation() {
        String service = getPageParameters().get(PARAM_SERVICE).toString();

        PageParameters pageParams = new PageParameters();
        pageParams.add(PARAM_SERVICE, service);

        return pageParams;
    }

    protected IModel<String> getBreadcrumbTitleModel() {
        String serviceLabel = getServiceLabel();
        if (entityId == null) {
            return Model.of(MessageFormat.format(getString("breadcrumb.title.add"), serviceLabel));
        } else {
            return Model.of(MessageFormat.format(getString("breadcrumb.title.edit"), serviceLabel));
        }
    }

}
