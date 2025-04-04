/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.wicket.page.edit;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.util.Attributes;
import org.devgateway.toolkit.forms.wicket.components.buttons.ladda.LaddaAjaxButton;
import nl.dries.wicket.hibernate.dozer.DozerModel;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.exceptions.NullJpaServiceException;
import org.devgateway.toolkit.forms.exceptions.NullListPageClassException;
import org.devgateway.toolkit.forms.util.MarkupCacheService;
import org.devgateway.toolkit.forms.wicket.components.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapCancelButton;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapDeleteButton;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapSubmitButton;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.visitors.GenericBootstrapValidationVisitor;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import jakarta.persistence.EntityManager;
import java.io.Serializable;

/**
 * @author mpostelnicu Page used to make editing easy, extend to get easy access
 * to one entity for editing
 */
public abstract class AbstractEditPage<T extends GenericPersistable & Serializable> extends BasePage {
    private static final long serialVersionUID = -5928614890244382103L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractEditPage.class);

    @SpringBean
    protected AdminSettingsService adminSettingsService;

    /**
     * Factory method for the new instance of the entity being editing. This
     * will be invoked only when the parameter PARAM_ID is null
     *
     * @return
     */
    private T newInstance() {
        return jpaService.newInstance();
    }

    /**
     * The repository used to fetch and save the entity, this is initialized in
     * subclasses
     */
    protected BaseJpaService<T> jpaService;

    /**
     * The page that is responsible for listing the entities (used here as a
     * return reference after successful save)
     */
    protected Class<? extends BasePage> listPageClass;

    /**
     * The form used by all subclasses
     */
    protected EditForm editForm;

    /**
     * the entity id, or null if a new entity is requested
     */
    protected Long entityId;

    /**
     * This is a wrapper model that ensures we can easily edit the properties of
     * the entity
     */
    private CompoundPropertyModel<T> compoundModel;

    /**
     * generic submit button for the form
     */
    protected BootstrapSubmitButton saveButton;

    /**
     * generic delete button for the form
     */
    protected BootstrapDeleteButton deleteButton;

    protected TextContentModal deleteModal;

    protected TextContentModal cancelModal;

    protected TextContentModal deleteFailedModal;

    protected TextContentModal saveFailedModal;

    protected TextContentModal failedModal;

    @SpringBean
    protected EntityManager entityManager;

    @SpringBean(required = false)
    private MarkupCacheService markupCacheService;

    public EditForm getEditForm() {
        return editForm;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void flushReportingCaches() {

        if (markupCacheService != null) {
            markupCacheService.flushMarkupCache();
            markupCacheService.clearPentahoReportsCache();
            markupCacheService.clearAllCaches();
        }
    }

    public Class<?> getNewInstanceClass() {
        return newInstance().getClass();
    }

    public GenericBootstrapValidationVisitor getBootstrapValidationVisitor(final AjaxRequestTarget target) {
        return new GenericBootstrapValidationVisitor(target);
    }

    protected TextContentModal createDeleteModal() {
        final TextContentModal modal = new TextContentModal("deleteModal",
                Model.of("Are you sure you want to delete? You will no longer be able to access this data."));

        final LaddaAjaxButton deleteButton = new LaddaAjaxButton("button", Buttons.Type.Danger) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);

                // close the modal
                deleteModal.appendCloseDialogJavaScript(target);
                onDelete(target);
            }
        };
        deleteButton.setDefaultFormProcessing(false);
        deleteButton.setLabel(Model.of("Yes"));
        modal.addCloseButton(Model.of("No"));
        modal.addButton(deleteButton);

        return modal;
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

    protected TextContentModal createDeleteFailedModal() {
        final TextContentModal modal = new TextContentModal("deleteFailedModal",
                new ResourceModel("delete_error_message"));
        modal.header(new ResourceModel("error"));
        final LaddaAjaxButton deleteButton = new LaddaAjaxButton("button", Buttons.Type.Info) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                setResponsePage(listPageClass, getCancelPageParameters());
            }
        };
        deleteButton.setDefaultFormProcessing(false);
        deleteButton.setLabel(Model.of("OK"));
        modal.addButton(deleteButton);

        modal.add(new AjaxEventBehavior("hidden.bs.modal") {
            @Override
            protected void onEvent(final AjaxRequestTarget target) {
                setResponsePage(listPageClass, getCancelPageParameters());
            }
        });

        return modal;
    }

    protected TextContentModal createSaveFailedModal() {
        final TextContentModal modal = new TextContentModal("saveFailedModal",
                new ResourceModel("optimistic_lock_error_message"));
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
                setResponsePage(listPageClass, getPageParameters());
            }
        });

        return modal;
    }

    protected TextContentModal createFailedModal() {
        final TextContentModal modal = new TextContentModal("failedModal",
                new ResourceModel("failedMessage"));
        modal.header(new ResourceModel("error"));
        final LaddaAjaxButton okButton = new LaddaAjaxButton("button", Buttons.Type.Info) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                setResponsePage(listPageClass, getPageParameters());
            }
        };
        okButton.setDefaultFormProcessing(false);
        okButton.setLabel(Model.of("OK"));
        modal.addButton(okButton);

        modal.add(new AjaxEventBehavior("hidden.bs.modal") {
            @Override
            protected void onEvent(final AjaxRequestTarget target) {
                setResponsePage(listPageClass, getPageParameters());
            }
        });

        return modal;
    }

    protected void afterSaveEntity(final T saveable) {
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

            deleteButton = getDeleteEditPageButton();
            add(deleteButton);

            deleteModal = createDeleteModal();
            add(deleteModal);

            cancelModal = createCancelModal();
            add(cancelModal);

            deleteFailedModal = createDeleteFailedModal();
            add(deleteFailedModal);

            saveFailedModal = createSaveFailedModal();
            add(saveFailedModal);

            failedModal = createFailedModal();
            add(failedModal);

            // don't display the delete button if we just create a new entity
            if (entityId == null) {
                deleteButton.setVisibilityAllowed(false);
            }

            add(getCancelButton("cancel"));
        }
    }

    protected BootstrapCancelButton getCancelButton(final String id) {
        return new BootstrapCancelButton(id, new StringResourceModel("cancelButton", this, null)) {
            private static final long serialVersionUID = -249084359200507749L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                setResponsePage(listPageClass, getPageParameters());
            }
        };
    }

    /**
     * Generic functionality for the save page button, this can be extended
     * further by subclasses
     *
     * @author mpostelnicu
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
                T saveable = editForm.getModelObject();

                beforeSaveEntity(saveable);

                // saves the entity and flushes the changes
                jpaService.saveAndFlush(saveable);

                // clears session and detaches all entities that are currently
                // attached
                entityManager.clear();

                // we flush the mondrian/wicket/reports cache to ensure it gets rebuilt
                flushReportingCaches();

                afterSaveEntity(saveable);

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
        return null;
    }

    protected PageParameters getCancelPageParameters() {
        return null;
    }

    protected void beforeSaveEntity(final T saveable) {
    }

    protected void beforeDeleteEntity(final T deleteable) {
    }

    public class DeleteEditPageButton extends BootstrapDeleteButton {
        private static final long serialVersionUID = 865330306716770506L;

        public DeleteEditPageButton(final String id, final IModel<String> model) {
            super(id, model);
        }

        @Override
        protected void onSubmit(final AjaxRequestTarget target) {
            deleteModal.show(true);
            target.add(deleteModal);
        }

        @Override
        protected void onError(final AjaxRequestTarget target) {
            super.onError(target);
            target.add(feedbackPanel);
        }

        @Override
        protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {

        }
    }

    /**
     * Override this to create new save buttons with additional behaviors
     *
     * @return
     */
    protected SaveEditPageButton getSaveEditPageButton() {
        return new SaveEditPageButton("save", new StringResourceModel("saveButton", this, null));
    }

    /**
     * Override this to create new delete buttons if you need additional
     * behavior
     *
     * @return
     */
    public DeleteEditPageButton getDeleteEditPageButton() {
        return new DeleteEditPageButton("delete", new StringResourceModel("deleteButton", this, null));
    }

    public AbstractEditPage(final PageParameters parameters) {
        super(parameters);

        if (!parameters.get(WebConstants.PARAM_ID).isNull()) {
            entityId = parameters.get(WebConstants.PARAM_ID).toLongObject();
        }

        editForm = new EditForm("editForm") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);

                if (ComponentUtil.isViewMode()) {
                    Attributes.addClass(tag, "print-view");
                }
            }
        };

        // use this in order to avoid "ServletRequest does not contain multipart
        // content" error
        // this error appears when we have a file upload component that is
        // hidden or not present in the page when the form is created
        editForm.setMultiPart(true);

        add(editForm);

        // this fragment ensures extra buttons are added below the wicket:child
        // section in child
        Fragment fragment = new Fragment("extraButtons", "noButtons", this);
        editForm.add(fragment);

        pageTitle.setDefaultModel(getTitleModel());

    }

    protected void onDelete(final AjaxRequestTarget target) {
        final T deleteable = editForm.getModelObject();
        try {
            beforeDeleteEntity(deleteable);

            jpaService.delete(deleteable);

            // we flush the mondrian/wicket/reports cache to ensure it gets rebuilt
            flushReportingCaches();
        } catch (DataIntegrityViolationException e) {
            deleteFailedModal.show(true);
            target.add(deleteFailedModal);
            return;
        }
        setResponsePage(listPageClass);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // we cant do anything if we dont have a jpaService here
        if (jpaService == null) {
            throw new NullJpaServiceException();
        }

        // we dont like receiving null list pages
        if (listPageClass == null) {
            throw new NullListPageClassException();
        }

        IModel<T> model = null;

//        if (entityId != null) {
//            T entity = jpaService.findById(entityId).orElse(null);
//            model = new CompoundPropertyModel<>(entity);
//        } else {
//            T instance = newInstance();
//            if (instance != null) {
//                model = new CompoundPropertyModel<>(instance);
//            }
//        }

        if (entityId != null) {
            model = new DozerModel<>(jpaService.findById(entityId).orElse(null));
        } else {
            T instance = newInstance();
            if (instance != null) {
                model = new DozerModel<>(instance);
            }
        }

        if (model != null) {
            editForm.setCompoundPropertyModel(model);
        }

        afterLoad(model);
    }

    protected void afterLoad(final IModel<T> model) {
    }

    protected StringResourceModel getTitleModel() {
        if (entityId == null) {
            return new StringResourceModel("page.title.add", this, null);
        } else {
            return new StringResourceModel("page.title.edit", this, null);
        }
    }
}
