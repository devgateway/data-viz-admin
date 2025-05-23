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
package org.devgateway.toolkit.forms.wicket.components.form;

import static org.apache.commons.io.FilenameUtils.getExtension;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipConfig;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.fileinput.BootstrapFileInput;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.fileinput.FileInputConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import de.agilecoders.wicket.jquery.Key;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.util.FileTypeUtil;
import org.devgateway.toolkit.forms.wicket.components.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.components.util.CustomDownloadLink;
import org.devgateway.toolkit.forms.wicket.events.EditingDisabledEvent;
import org.devgateway.toolkit.forms.wicket.events.EditingEnabledEvent;
import org.devgateway.toolkit.persistence.dao.FileContent;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author idobre
 * @since 11/13/14
 * <p>
 * Multi upload file component that acts as a form component
 */

public class FileInputBootstrapFormComponentWrapper<T> extends FormComponentPanel<T> {
    private static final long serialVersionUID = 1L;

    private final Logger logger = LoggerFactory.getLogger(FileInputBootstrapFormComponentWrapper.class);

    private Collection<FileMetadata> filesModel;

    private int maxFiles = 0;

    private final NotificationPanel fileUploadFeedback = new NotificationPanel("fileUploadFeedback");

    private static final TooltipConfig TOOLTIP_CONFIG =
            new TooltipConfig().withPlacement(TooltipConfig.Placement.bottom);

    private WebMarkupContainer alreadyUploadedFiles;

    private WebMarkupContainer pendingFiles;

    private BootstrapFileInput bootstrapFileInput;

    private Boolean visibleOnlyToAdmin = false;

    private Boolean disableDeleteButton = false;

    private boolean allowDownloadWhenReadonly = true;

    private boolean requireAtLeastOneItem = false;

    /**
     * File name extensions that are allowed to upload. Must be lowercase and without the dot. Ex: pdf
     */
    private List<String> allowedFileExtensions = new ArrayList<>();

    private transient IModel<List<FileUpload>> internalUploadModel;


    public FileInputBootstrapFormComponentWrapper(final String id, final IModel<T> model) {
        super(id, model);

        setOutputMarkupId(true);
        setRenderBodyOnly(true); // we need this because bootstrap is adding
        // unnecessary classes to the component
    }

    public FileInputBootstrapFormComponentWrapper<T> maxFiles(final int maxFiles) {
        this.maxFiles = maxFiles;

        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (getModel().getObject() == null) {
            getModel().setObject((T) new HashSet<FileMetadata>());
        }

        filesModel = (Collection<FileMetadata>) getModel().getObject();

        addAlreadyUploadedFilesComponent();
        addPendingFilesComponent();
        addFileUploadFeedbackComponent();
        addBootstrapFileInputComponent();

        bootstrapFileInput.withShowUpload(true).withShowRemove(false).withShowPreview(false).withShowCaption(true);
    }

    public boolean isVisibleAlreadyUploadedFiles() {
        return filesModel != null && filesModel.size() > 0;
    }

    /**
     * already uploaded files section
     */
    private void addAlreadyUploadedFilesComponent() {
        alreadyUploadedFiles = new WebMarkupContainer("alreadyUploadedFiles") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onInitialize() {
                super.onInitialize();
                setVisibilityAllowed(isVisibleAlreadyUploadedFiles());
            }
        };

        alreadyUploadedFiles.setOutputMarkupPlaceholderTag(true);
        alreadyUploadedFiles.setOutputMarkupId(true);
        add(alreadyUploadedFiles);

        alreadyUploadedFiles
                .add(new Label("uploadedFilesTitle", new StringResourceModel("uploadedFilesTitle", this, null)));

        IModel<List<FileMetadata>> alreadyUploadedFilesModel =
                new IModel<List<FileMetadata>>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public List<FileMetadata> getObject() {
                        List<FileMetadata> fileObject = new ArrayList<>();

                        // get only the already uploaded files
                        for (FileMetadata file : filesModel) {
                            if (!file.isNew()) {
                                fileObject.add(file);
                            }
                        }

                        return fileObject;
                    }
                };

        ListView<FileMetadata> list = new ListView<FileMetadata>("list", alreadyUploadedFilesModel) {
            private static final long serialVersionUID = 1L;

            private List<IndicatingAjaxLink<Void>> deleteButtons = new ArrayList<>();

            @Override
            protected void populateItem(final ListItem<FileMetadata> item) {
                // make file name clickable
                Link<FileMetadata> downloadLink = new Link<FileMetadata>("downloadLink", item.getModel()) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        final FileMetadata modelObject = getModelObject();

                        AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void write(final OutputStream output) throws IOException {
                                output.write(modelObject.getContent().getBytes());
                            }

                            @Override
                            public String getContentType() {
                                return modelObject.getContentType();
                            }
                        };


                        ResourceStreamRequestHandler handler =
                                new ResourceStreamRequestHandler(rstream, modelObject.getName());
                        handler.setContentDisposition(ContentDisposition.ATTACHMENT);
                        getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
                    }

                    @Override
                    public boolean isEnabledInHierarchy() {
                        return isLinkEnabledInHierarchy(this, super.isEnabledInHierarchy());
                    }
                };
                downloadLink.add(new Label("downloadText", item.getModelObject().getName()));
                downloadLink.add(new TooltipBehavior(new StringResourceModel("downloadUploadedFileTooltip",
                        FileInputBootstrapFormComponentWrapper.this, null), TOOLTIP_CONFIG));
                item.add(downloadLink);

                Link<FileMetadata> download = new CustomDownloadLink("download", item.getModel());
                item.add(download);

                IndicatingAjaxLink<Void> delete = new IndicatingAjaxLink<Void>("delete") {
                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        filesModel.remove(item.getModelObject());
                        FileInputBootstrapFormComponentWrapper.this.getModel().setObject((T) filesModel);

                        target.add(alreadyUploadedFiles);
                    }
                };
                delete.add(new IconBehavior(FontAwesome5IconType.trash_s));
                delete.add(new TooltipBehavior(new StringResourceModel("removeUploadedFileTooltip",
                        FileInputBootstrapFormComponentWrapper.this, null), TOOLTIP_CONFIG));

                delete.setVisible(true);
                item.add(delete);
                deleteButtons.add(delete);

                // there are situation when we want to display the delete button
                // only to admins
                if (visibleOnlyToAdmin) {
                    MetaDataRoleAuthorizationStrategy.authorize(delete, Component.RENDER,
                            SecurityConstants.Roles.ROLE_ADMIN);
                }
                if (disableDeleteButton) {
                    delete.setVisibilityAllowed(false);
                }
            }

            @Override
            public void onEvent(final IEvent<?> event) {
                /*
                 * disable 'delete' buttons based on the form state
                 */
                if (event.getPayload() instanceof EditingDisabledEvent) {
                    for (IndicatingAjaxLink<?> del : deleteButtons) {
                        del.setVisibilityAllowed(false);
                    }
                }
                if (event.getPayload() instanceof EditingEnabledEvent) {
                    for (IndicatingAjaxLink<?> del : deleteButtons) {
                        del.setVisibilityAllowed(true);
                    }
                }
            }
        };

        alreadyUploadedFiles.add(list);
    }

    /**
     * pending files section
     */
    private void addPendingFilesComponent() {
        pendingFiles = new WebMarkupContainer("pendingFiles") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();

                if (filesModel != null && filesModel.size() > 0) {
                    for (FileMetadata file : filesModel) {
                        if (file.isNew()) {
                            setVisibilityAllowed(true);

                            return;
                        }
                    }
                }

                setVisibilityAllowed(false);
            }
        };

        pendingFiles.setOutputMarkupPlaceholderTag(true);
        pendingFiles.setOutputMarkupId(true);
        add(pendingFiles);

        pendingFiles.add(new Label("pendingFilesTitle", new StringResourceModel("pendingFilesTitle", this, null)));

        IModel<List<FileMetadata>> pendingFilesModel = new IModel<List<FileMetadata>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<FileMetadata> getObject() {
                List<FileMetadata> fileObject = new ArrayList<>();

                // get only the files without an ID (this files are pending for
                // upload)
                for (FileMetadata file : filesModel) {
                    if (file.isNew()) {
                        fileObject.add(file);
                    }
                }

                return fileObject;
            }
        };

        ListView<FileMetadata> list = new ListView<FileMetadata>("list", pendingFilesModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<FileMetadata> item) {
                item.add(new Label("fileTitle", item.getModelObject().getName()));

                IndicatingAjaxLink<Void> delete = new IndicatingAjaxLink<Void>("delete") {
                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        filesModel.remove(item.getModelObject());
                        FileInputBootstrapFormComponentWrapper.this.getModel().setObject((T) filesModel);

                        target.add(pendingFiles);
                        FileInputBootstrapFormComponentWrapper.this.onUpdate(target);
                    }
                };
                delete.add(new IconBehavior(FontAwesome5IconType.trash_s));
                delete.add(new TooltipBehavior(new StringResourceModel("removeUploadedFileTooltip",
                        FileInputBootstrapFormComponentWrapper.this, null), TOOLTIP_CONFIG));

                delete.setVisible(true);
                item.add(delete);
            }
        };

        pendingFiles.add(list);
    }

    private void addFileUploadFeedbackComponent() {
        fileUploadFeedback.setOutputMarkupId(true);

        // show only the messages (fatal, success) generated by this component
        fileUploadFeedback.setFilter(new IFeedbackMessageFilter() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean accept(final FeedbackMessage message) {
                final Component reporter = message.getReporter();

                // try to avoid displaying the error messages that comes from
                // parent (GenericBootstrapFormComponent)
                // for example errors like 'FIELD is required.'
                if (message.getLevel() == FeedbackMessage.ERROR) {
                    return false;
                }

                return reporter != null && (FileInputBootstrapFormComponentWrapper.this.contains(reporter, true)
                        || Objects.equal(FileInputBootstrapFormComponentWrapper.this, reporter));
            }
        });
        add(fileUploadFeedback);
    }

    private void addBootstrapFileInputComponent() {
        // this is where the newly uploaded files are saved
        internalUploadModel = new ListModel<>();

        /*
         * some customization of the BootstrapFileInput Component
         */
        FileInputConfig fileInputConfig = new FileInputConfig();
        fileInputConfig.put(new Key<String>("browseLabel"),
                new StringResourceModel("browseLabel", FileInputBootstrapFormComponentWrapper.this, null).getString());
        fileInputConfig.put(new Key<String>("uploadClass"), "btn btn-blue");
        fileInputConfig.put(new Key<String>("browseClass"), "btn btn-blue");
        fileInputConfig.put(new Key<String>("browseIcon"), "<i class=\"fa fa-folder-open\"></i>");
        fileInputConfig.put(new Key<String>("uploadIcon"), "<i class=\"fa fa-upload\"></i>");

        bootstrapFileInput = new BootstrapFileInput("bootstrapFileInput", internalUploadModel, fileInputConfig) {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);

                List<FileUpload> fileUploads = internalUploadModel.getObject();
                try {
                    if (fileUploads != null) {
                        // Existing validation checks
                        if (maxFiles > 0 && filesModel.size() + fileUploads.size() > maxFiles) {
                            if (maxFiles == 1) {
                                FileInputBootstrapFormComponentWrapper.this.fatal(new StringResourceModel("OneUpload",
                                        FileInputBootstrapFormComponentWrapper.this, null).getString());
                            } else {
                                FileInputBootstrapFormComponentWrapper.this.fatal(new StringResourceModel("tooManyFiles",
                                        FileInputBootstrapFormComponentWrapper.this, Model.of(maxFiles)).getString());
                            }
                            FileInputBootstrapFormComponentWrapper.this.invalid();
                        } else if (!fileContentsAndExtensionsAreValid(fileUploads)) {
                            String error = new StringResourceModel("fileContentsDoNotMatchExtension",
                                    FileInputBootstrapFormComponentWrapper.this, null).getString();
                            FileInputBootstrapFormComponentWrapper.this.fatal(error);
                            FileInputBootstrapFormComponentWrapper.this.invalid();
                        } else if (!fileExtensionAreAllowed(fileUploads)) {
                            String error = new StringResourceModel("fileTypeNotAllowed",
                                    FileInputBootstrapFormComponentWrapper.this, null)
                                    .setParameters(String.join(", ", allowedFileExtensions))
                                    .getString();
                            FileInputBootstrapFormComponentWrapper.this.fatal(error);
                            FileInputBootstrapFormComponentWrapper.this.invalid();
                        } else {
                            // Process uploads
                            for (FileUpload upload : fileUploads) {
                                processAndStoreUpload(upload);
                            }
                        }
                    }



                    FileInputBootstrapFormComponentWrapper.this.getModel().setObject((T) filesModel);

                    target.add(fileUploadFeedback);
                    target.add(pendingFiles);
                    target.appendJavaScript("$('.cover-buttons-div').css(\"z-index\", -1);");
                    FileInputBootstrapFormComponentWrapper.this.onUpdate(target);

                    // Clear the upload model
                    internalUploadModel.setObject(null);

                } catch (Exception e) {
                    logger.error("Error processing file upload", e);
                    FileInputBootstrapFormComponentWrapper.this.error("Error processing upload: " + e.getMessage());
                    target.add(fileUploadFeedback);
                }
            }
        };

        add(bootstrapFileInput);

        /**
         * due to an upgrade of FormGroup in wicket7/wicket-bootrap-0.10, the
         * visitor that finds inner FormComponentS, will now find two instead of
         * one: the FileInputBootstrapFormComponentWrapper and also the
         * BootstrapFileInputField. This is the RIGHT result, previously in
         * wicket 6.x it only got the first level of children, hence only one
         * FormComponent (the FileInputBootstrapFormComponentWrapper). It would
         * then read the label from FileInputBootstrapFormComponentWrapper and
         * use it for displaying the label of the FormGroup. In
         * wicket7/wicket-bootstrap-0.10 this will result in reading the label
         * of BootstrapFileInputField which is null. So you will notice no
         * labels for FormGroupS. We fix this by forcing the label of the
         * underlying fileInput element to the same model as the label used by
         * FileInputBootstrapFormComponentWrapper
         */
        FormComponent<?> fileInput = (FormComponent<?>) bootstrapFileInput.get("fileInputForm").get("fileInput");
        fileInput.setLabel(this.getLabel());

        // there are situation when we want to display the upload file componentadmin_settings_aud
        // only to admins
        if (visibleOnlyToAdmin) {
            MetaDataRoleAuthorizationStrategy.authorize(bootstrapFileInput, Component.RENDER,
                    SecurityConstants.Roles.ROLE_ADMIN);
        }

        // for download the documents when you're already signed in as admin and
        // want to read only
        if (disableDeleteButton) {
            MetaDataRoleAuthorizationStrategy.authorize(bootstrapFileInput, Component.RENDER,
                    MetaDataRoleAuthorizationStrategy.NO_ROLE);
        }
    }

    private void processAndStoreUpload(FileUpload upload) {
        try {
            FileMetadata fileMetadata = new FileMetadata();
            fileMetadata.setName(upload.getClientFileName());
            fileMetadata.setContentType(upload.getContentType());
            fileMetadata.setSize(upload.getSize());

            FileContent fileContent = new FileContent();
            fileContent.setBytes(upload.getBytes());
            fileMetadata.setContent(fileContent);

            filesModel.add(fileMetadata);
        } catch (Exception e) {
            logger.error("Error processing file upload", e);
            throw e;
        } finally {
            upload.closeStreams();  // Clean up resources
        }
    }
    private boolean fileExtensionAreAllowed(List<FileUpload> fileUploads) {
        if (allowedFileExtensions.isEmpty()) {
            return true;
        }
        return fileUploads.stream().allMatch(
                u -> allowedFileExtensions.contains(getExtension(u.getClientFileName()).toLowerCase()));
    }

    private boolean fileContentsAndExtensionsAreValid(List<FileUpload> fileUploads) {
        try {
            for (FileUpload upload : fileUploads) {
                String fileName = upload.getClientFileName();
                InputStream inputStream = new ByteArrayInputStream(upload.getBytes());
                if (!FileTypeUtil.extensionMatchesContents(fileName, inputStream)) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            logger.error("Failed to check if file contents match file name.", e);
            return false;
        }
    }

    @Override
    public void onEvent(final IEvent<?> event) {
        ComponentUtil.enableDisableEvent(this, event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void convertInput() {
        final Collection<FileMetadata> modelObject = filesModel;

        setConvertedInput((T) modelObject);

        /*
         * if we still have issues like CCR-310 then we will need to update the
         * files setters like: if (this.upload == null) { this.upload = upload;
         * } else { this.upload.clear(); if(upload != null) {
         * this.upload.addAll(upload); } }
         */
    }

    protected void onUpdate(final AjaxRequestTarget target) {
        pendingFiles.remove();
        fileUploadFeedback.remove();

        addPendingFilesComponent();
        addFileUploadFeedbackComponent();
    }

    public void setVisibleOnlyToAdmin(final Boolean visibleOnlyToAdmin) {
        this.visibleOnlyToAdmin = visibleOnlyToAdmin;
    }

    public Boolean getDisableDeleteButton() {
        return disableDeleteButton;
    }

    public void setDisableDeleteButton(final Boolean disableDeleteButton) {
        this.disableDeleteButton = disableDeleteButton;
    }

    public WebMarkupContainer getAlreadyUploadedFiles() {
        return alreadyUploadedFiles;
    }


    public void requireAtLeastOneItem() {
        requireAtLeastOneItem = true;
    }

    public boolean isAllowDownloadWhenReadonly() {
        return allowDownloadWhenReadonly;
    }

    public void setAllowDownloadWhenReadonly(final boolean allowDownloadWhenReadonly) {
        this.allowDownloadWhenReadonly = allowDownloadWhenReadonly;
    }

    private boolean isLinkEnabledInHierarchy(final Component component, final boolean defaultState) {
        if (allowDownloadWhenReadonly) {
            return component.isEnableAllowed() && component.isEnabled();
        }
        return defaultState;
    }

    @Override
    public boolean checkRequired() {
        return !requireAtLeastOneItem || !ObjectUtils.isEmpty(getModelObject());
    }

    public void setAllowedFileExtensions(List<String> allowedFileExtensions) {
        this.allowedFileExtensions = allowedFileExtensions;
    }
}
