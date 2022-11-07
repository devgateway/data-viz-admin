package org.devgateway.toolkit.forms.wicket.components;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.providers.ListDataProvider;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Simplified pageable table panel
 * @param <T> the child element
 * @param <PARENT> the parent element
 *
 * @author Nadejda Mandrescu
 */
public class PageableTablePanel<T extends Serializable, PARENT extends Serializable>
        extends CompoundSectionPanel<List<T>> {
    private static final long serialVersionUID = -7808948052573265743L;

    protected final IModel<PARENT> parentModel;

    @SpringBean
    AdminSettingsService adminSettingsService;

    protected AjaxFallbackBootstrapDataTable dataTable;

    protected ISortableDataProvider<T, String> dataProvider;

    protected List<IColumn<T, String>> columns = new ArrayList<>();

    public PageableTablePanel(final String id, final IModel<PARENT> parentModel) {
        super(id, new PropertyModel<List<T>>(parentModel, id));
        this.parentModel = parentModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (dataProvider == null) {
            dataProvider = new ListDataProvider<>(getModel());
        }
        setOutputMarkupPlaceholderTag(true);

        add(new Label("panelTitle", title));

        add(buildDataTable());
    }

    protected AjaxFallbackBootstrapDataTable buildDataTable() {
        dataTable = new AjaxFallbackBootstrapDataTable("table", columns, dataProvider, getPageSize());
        return dataTable;
    }

    private Integer getPageSize() {
        return adminSettingsService.get().getPageSize();
    }

    public AjaxFallbackBootstrapDataTable getDataTable() {
        return dataTable;
    }

    public void setSort(final Object property, final SortOrder sortOrder) {
        ((ListDataProvider) dataProvider).setSort(property, SortOrder.ASCENDING);
    }

    protected List<T> getItems() {
        return getModelObject();
    }

    public void addColumnReadonly(final String resourceKey, final String property) {
        addColumnReadonly(resourceKey, null, property, null);
    }

    public void addSortableColumnReadonly(final String resourceKey, final String property) {
        addColumnReadonly(resourceKey, property, property, null);
    }

    public void addColumnReadonly(final String resourceKey, final String sortProperty, final String property,
            final String css) {
        columns.add(new PropertyColumn<T, String>(new StringResourceModel(resourceKey), sortProperty, property) {
            private static final long serialVersionUID = -3578903469355827616L;

            @Override
            public String getCssClass() {
                return css;
            }
        });
    }
}
