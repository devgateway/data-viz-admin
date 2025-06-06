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
package org.devgateway.toolkit.forms.wicket.page.lists;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Size;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.devgateway.toolkit.forms.wicket.components.buttons.ladda.LaddaAjaxButton;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilteredColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.exceptions.NullEditPageClassException;
import org.devgateway.toolkit.forms.exceptions.NullJpaServiceException;
import org.devgateway.toolkit.forms.wicket.components.form.AJAXDownload;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.components.table.DataTableAware;
import org.devgateway.toolkit.forms.wicket.components.table.ResettingFilterForm;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.RevisionsPage;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.providers.SortableJpaServiceDataProvider;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.excel.service.ExcelGeneratorService;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author mpostelnicu This class can be use to display a list of Categories
 * <p>
 * T - entity type
 */
public abstract class AbstractListPage<T extends GenericPersistable & Serializable> extends BasePage {
    private static final long serialVersionUID = 1958350868666244087L;

    @SpringBean
    AdminSettingsService adminSettingsService;

    protected Class<? extends AbstractEditPage<T>> editPageClass;

    protected AjaxFallbackBootstrapDataTable<T, String> dataTable;

    protected List<IColumn<T, String>> columns;

    protected BaseJpaService<T> jpaService;

    protected SortableJpaServiceDataProvider<T> dataProvider;

    protected BootstrapBookmarkablePageLink<T> editPageLink;

    protected Boolean filterGoReset = false;

    protected Form excelForm;

    @SpringBean
    private ExcelGeneratorService excelGeneratorService;

    public AbstractListPage(final PageParameters parameters) {
        super(parameters);

        columns = new ArrayList<>();
        columns.add(new PropertyColumn<>(new Model<>("ID"), "id", "id"));
    }

    public ActionPanel getActionPanel(final String id, final IModel<T> model) {
        return new ActionPanel(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Fragment("topPage", "topPageTitleFragment", this));

        Fragment fragment = new Fragment("bottomPageFragment", "noBottomPageFragment", this);
        add(fragment);

        add(new Fragment("bottomPage", "bottomPageTitleFragment", this));

        if (jpaService == null) {
            throw new NullJpaServiceException();
        }
        if (editPageClass == null) {
            throw new NullEditPageClassException();
        }

        dataProvider = new SortableJpaServiceDataProvider<>(jpaService, getPageSize());
        dataProvider.setFilterState(newFilterState());

        // create the excel download form; by default this form is hidden and we should make it visible only to pages
        // where we want to export entities to excel file
        excelForm = new ExcelDownloadForm("excelForm");
        excelForm.setVisibilityAllowed(false);
        add(excelForm);

        // add the 'Edit' button
        columns.add(new AbstractColumn<T, String>(new StringResourceModel("actionsColumn", this, null)) {
            private static final long serialVersionUID = -7447601118569862123L;

            @Override
            public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId,
                                     final IModel<T> model) {
                cellItem.add(getActionPanel(componentId, model));
            }
        });
        dataTable = new AjaxFallbackBootstrapDataTable<>("table", columns, dataProvider, getPageSize());

        ResettingFilterForm<JpaFilterState<T>> filterForm =
                new ResettingFilterForm<>("filterForm", dataProvider, dataTable);
        filterForm.add(dataTable);
        add(filterForm);

        if (hasFilteredColumns()) {
            FilterToolbar filterToolbar = null;
            if (filterGoReset) {
                GoAndClearFilter go = new BootstrapGoClearFilter("go", filterForm);
                filterToolbar = new GoFilterToolbar(dataTable, go, filterForm);
            } else {
                filterToolbar = new FilterToolbar(dataTable, filterForm);
            }
            dataTable.addTopToolbar(filterToolbar);
            setDataTableForFilteredColumns();
        }

        PageParameters pageParameters = getEditPageParameters();

        editPageLink = new BootstrapBookmarkablePageLink<T>("new", editPageClass, pageParameters, Buttons.Type.Success);
        editPageLink.setIconType(FontAwesome5IconType.plus_circle_s).setSize(Size.Large)
                .setLabel(new StringResourceModel("new", AbstractListPage.this, null));
        editPageLink.setOutputMarkupId(true);

        add(editPageLink);
    }

    private Integer getPageSize() {
        return adminSettingsService.get() != null ? adminSettingsService.get().getPageSize() : 10;
    }

    protected PageParameters getEditPageParameters() {
        PageParameters pageParameters = new PageParameters();
        pageParameters.set(WebConstants.PARAM_ID, null);
        return pageParameters;
    }

    public class ActionPanel extends GenericPanel<T> {
        private static final long serialVersionUID = 5821419128121941939L;
        protected PageParameters pageParameters;
        protected BootstrapBookmarkablePageLink<T> editItemPageLink;

        /**
         * @param id
         * @param model
         */
        public ActionPanel(final String id, final IModel<T> model) {
            super(id, model);

            final PageParameters pageParameters = new PageParameters();

            @SuppressWarnings("unchecked")
            T entity = (T) ActionPanel.this.getDefaultModelObject();
            if (entity != null) {
                pageParameters.set(WebConstants.PARAM_ID, entity.getId());
            }
            addEditLinkPageParameters(pageParameters);

            editItemPageLink =
                    new BootstrapBookmarkablePageLink<>("edit", editPageClass, pageParameters, Buttons.Type.Info);
            editItemPageLink.setIconType(FontAwesome5IconType.edit_s).setSize(Size.Small)
                    .setLabel(new StringResourceModel("edit", AbstractListPage.this, null));
            add(editItemPageLink);


            add(getPrintButton(pageParameters));
            add(getRevisionsLink(entity));
        }
    }

    protected void addEditLinkPageParameters(final PageParameters pageParameters) {

    }

    protected Component getRevisionsLink(final T entity) {
        PageParameters revisionsPageParameters = new PageParameters();
        revisionsPageParameters.set(WebConstants.PARAM_ID, entity.getId());
        revisionsPageParameters.set(WebConstants.PARAM_ENTITY_CLASS, entity.getClass().getName());

        BootstrapBookmarkablePageLink<Void> revisionsPageLink = new BootstrapBookmarkablePageLink<>("revisions",
                RevisionsPage.class, revisionsPageParameters, Buttons.Type.Info);
        revisionsPageLink.setIconType(FontAwesome5IconType.clock_s).setSize(Size.Small)
                .setLabel(new StringResourceModel("revisions", AbstractListPage.this, null));

        return revisionsPageLink;
    }

    /**
     * Get a stub print button that does nothing
     *
     * @param pageParameters
     * @return
     */
    protected Component getPrintButton(final PageParameters pageParameters) {
        return new WebMarkupContainer("printButton").setVisibilityAllowed(false);
    }

    private boolean hasFilteredColumns() {
        for (IColumn<?, ?> column : columns) {
            if (column instanceof IFilteredColumn) {
                return true;
            }
        }
        return false;
    }

    private void setDataTableForFilteredColumns() {
        for (IColumn<?, ?> column : columns) {
            if (column instanceof IFilteredColumn && column instanceof DataTableAware) {
                if (((DataTableAware) column).getDataTable() == null) {
                    ((DataTableAware) column).setDataTable(dataTable);
                }
            }
        }
    }

    public JpaFilterState<T> newFilterState() {
        return new JpaFilterState<>();
    }

    /**
     * A wrapper form that is used to fire the excel download action
     */
    public class ExcelDownloadForm extends Form<Void> {
        public ExcelDownloadForm(final String id) {
            super(id);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            final AJAXDownload download = new AJAXDownload() {
                @Override
                protected IRequestHandler getHandler() {
                    return new IRequestHandler() {
                        @Override
                        public void respond(final IRequestCycle requestCycle) {
                            final HttpServletResponse response = (HttpServletResponse) requestCycle
                                    .getResponse().getContainerResponse();

                            try {
                                final int batchSize = 10000;

                                final long count = excelGeneratorService.count(
                                        jpaService,
                                        dataProvider.getFilterState().getSpecification());

                                // if we need to export just one file then we don't create an archive
                                if (count <= batchSize) {
                                    // set a maximum download of objects per excel file
                                    final PageRequest pageRequest = PageRequest.of(0, batchSize,
                                            Sort.Direction.ASC, "id");

                                    final byte[] bytes = excelGeneratorService.getExcelDownload(
                                            jpaService,
                                            dataProvider.getFilterState().getSpecification(),
                                            pageRequest);

                                    response.setContentType(
                                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                                    response.setHeader("Content-Disposition", "attachment; filename=excel-export.xlsx");
                                    response.getOutputStream().write(bytes);
                                } else {
                                    response.setContentType("application/zip");
                                    response.setHeader("Content-Disposition", "attachment; filename=excel-export.zip");
                                    response.flushBuffer();
                                    final ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(
                                            response.getOutputStream()));
                                    zout.setMethod(ZipOutputStream.DEFLATED);
                                    zout.setLevel(Deflater.NO_COMPRESSION);
                                    final int numberOfPages = (int) Math.ceil((double) count / batchSize);
                                    for (int i = 0; i < numberOfPages; i++) {
                                        final PageRequest pageRequest = PageRequest.of(i, batchSize,
                                                Sort.Direction.ASC, "id");
                                        final byte[] bytes = excelGeneratorService.getExcelDownload(
                                                jpaService,
                                                dataProvider.getFilterState().getSpecification(),
                                                pageRequest);
                                        final ZipEntry ze = new ZipEntry("excel-export-page " + (i + 1) + ".xlsx");
                                        zout.putNextEntry(ze);
                                        zout.write(bytes, 0, bytes.length);
                                        zout.closeEntry();
                                        response.flushBuffer();
                                    }
                                    zout.close();
                                    response.flushBuffer();
                                }
                            } catch (IOException e) {
                                logger.error("Download error", e);
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
            add(download);

            final LaddaAjaxButton excelButton = new LaddaAjaxButton("excelButton",
                    new Model<>("Excel Download"),
                    Buttons.Type.Warning) {
                @Override
                protected void onSubmit(final AjaxRequestTarget target) {
                    super.onSubmit(target);

                    // initiate the file download
                    download.initiate(target);
                }
            };
            excelButton.setIconType(FontAwesome5IconType.file_excel_s);
            add(excelButton);
        }
    }
}
