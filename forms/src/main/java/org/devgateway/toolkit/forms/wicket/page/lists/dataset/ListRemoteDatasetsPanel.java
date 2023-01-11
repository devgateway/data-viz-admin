package org.devgateway.toolkit.forms.wicket.page.lists.dataset;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.filter.BootstrapTextFilteredPropertyColumn;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.service.admin.ServiceDatasetService;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.components.table.ResettingFilterForm;
import org.devgateway.toolkit.forms.wicket.components.table.filter.ServiceDatasetFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.ServiceEntityFilterState;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ServiceDatasetProvider;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.dto.ServiceDataset;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListRemoteDatasetsPanel extends Panel {

    private String serviceName;

    @SpringBean
    protected AdminSettingsService adminSettingsService;

    protected AjaxFallbackBootstrapDataTable<ServiceDataset, String> dataTable;

    protected ServiceDatasetProvider dataProvider;

    protected List<IColumn<ServiceDataset, String>> columns;

    @SpringBean
    private ServiceDatasetService serviceDatasetService;

    protected int pageRowNo = 0;

    private Component dialogModalComponent;

    public ListRemoteDatasetsPanel(final String id) {
        super(id);
    }

    public ListRemoteDatasetsPanel(final String id, final String serviceName) {
        super(id);
        this.serviceName = serviceName;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        columns = new ArrayList<>();
        columns.add(new BootstrapTextFilteredPropertyColumn<>(new Model<>("Name"), "value", "value", "value"));
        columns.add(new PropertyColumn<ServiceDataset, String>(new Model<>("Date created"), "createdDate", "createdDate") {
            @Override
            public IModel<?> getDataModel(final IModel<ServiceDataset> rowModel) {
                IModel<?> model = super.getDataModel(rowModel);
                ZonedDateTime createdDate = (ZonedDateTime) model.getObject();
                return Model.of(createdDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            }
        });
        columns.add(new AbstractColumn<ServiceDataset, String>(new StringResourceModel("actionsColumn", this, null)) {
            private static final long serialVersionUID = -6185373434402515868L;

            @Override
            public void populateItem(final Item<ICellPopulator<ServiceDataset>> cellItem, final String componentId,
                                     final IModel<ServiceDataset> model) {
                cellItem.add(new RemoteDatasetActionPanel(componentId, model) {
                    @Override
                    protected void onConfirmationSubmit(final AjaxRequestTarget target) {
                        serviceDatasetService.delete(serviceName, model.getObject());
                        target.add(dataTable);
                    }

                    @Override
                    protected void onOpenModal(final AjaxRequestTarget target, final Modal modal) {
                        dialogModalComponent = dialogModalComponent.replaceWith(modal);
                        target.add(dialogModalComponent);
                    }
                });
            }

            @Override
            public String getCssClass() {
                return (StringUtils.isNotBlank(super.getCssClass()) ? super.getCssClass() : "") + " col-md-1";
            }
        });

        dataProvider = new ServiceDatasetProvider(serviceDatasetService, serviceName);
        dataProvider.setFilterState(new ServiceDatasetFilterState());

        dataTable = new AjaxFallbackBootstrapDataTable<ServiceDataset, String>(
                "table", columns, dataProvider, getPageSize()) {
            private static final long serialVersionUID = 632539691822305263L;

            @Override
            protected void onPageChanged() {
                pageRowNo = 0;
            }
        };

        ResettingFilterForm<ServiceEntityFilterState<ServiceDataset>> filterForm =
                new ResettingFilterForm<>("filterForm", dataProvider, dataTable);
        filterForm.add(dataTable);
        add(filterForm);

        addDialogModal();
    }

    protected void addDialogModal() {
        dialogModalComponent = new WebMarkupContainer("dialogModal");
        dialogModalComponent.setOutputMarkupPlaceholderTag(true);
        add(this.dialogModalComponent);
    }

    protected int getPageSize() {
        return adminSettingsService.get().getPageSize();
    }

    public ServiceEntityFilterState<ServiceDataset> newFilterServiceDatasetState() {
        return new ServiceDatasetFilterState();
    }
}
