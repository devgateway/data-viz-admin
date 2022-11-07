package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.exceptions.NullServiceEntityServiceException;
import org.devgateway.toolkit.forms.service.admin.BaseServiceEntityService;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.edit.admin.AbstractEditServiceEntityPage;
import org.devgateway.toolkit.persistence.dto.ServiceEntity;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AbstractListServiceEntityPage<T extends ServiceEntity> extends BasePage {

    private static final long serialVersionUID = 652587400391540726L;

    protected Class<? extends AbstractEditServiceEntityPage<T>> editPageClass;

    @SpringBean
    protected AdminSettingsService adminSettingsService;

    protected AjaxFallbackBootstrapDataTable<T, String> dataTable;

    protected SortableServiceEntityProvider<T> dataProvider;

    protected List<IColumn<T, String>> columns;

    protected BaseServiceEntityService<T> serviceEntityService;

    protected int pageRowNo = 0;

    public AbstractListServiceEntityPage(final PageParameters pageParameters) {
        super(pageParameters);

        columns = new ArrayList<>();
        columns.add(new PropertyColumn<>(new Model<>("Code"), "code", "code"));
        columns.add(new PropertyColumn<>(new Model<>("Value"), "value", "value"));
        columns.add(new PropertyColumn<>(new Model<>("Position"), "position", "position"));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (serviceEntityService == null) {
            throw new NullServiceEntityServiceException();
        }

        String serviceName = getPageParameters().get(WebConstants.PARAM_SERVICE).toString();
        dataProvider = new SortableServiceEntityProvider<>(serviceEntityService, serviceName);

        dataTable = new AjaxFallbackBootstrapDataTable<T, String>(
                "table", columns, dataProvider, getPageSize()) {
            private static final long serialVersionUID = 632539691822305263L;

            @Override
            protected void onPageChanged() {
                pageRowNo = 0;
            }
        };

        columns.add(new AbstractColumn<T, String>(new StringResourceModel("actionsColumn", this, null)) {
            private static final long serialVersionUID = -7447601118569862123L;

            @Override
            public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId,
                                     final IModel<T> model) {
                cellItem.add(getActionPanel(componentId, model));
            }
        });

        add(dataTable);
    }

    protected int getPageSize() {
        return adminSettingsService.get().getPageSize();
    }

    public ActionPanel getActionPanel(final String id, final IModel<T> model) {
        return new ActionPanel(id, model);
    }

    public class ActionPanel extends GenericPanel<T> {
        private static final long serialVersionUID = 5821419128121941939L;

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
            pageParameters.set(WebConstants.PARAM_SERVICE, getPageParameters().get(WebConstants.PARAM_SERVICE));

            editItemPageLink =
                    new BootstrapBookmarkablePageLink<>("edit", editPageClass, pageParameters, Buttons.Type.Info);
            editItemPageLink.setIconType(FontAwesome5IconType.edit_s).setSize(Buttons.Size.Small)
                    .setLabel(new StringResourceModel("edit", this, null));
            add(editItemPageLink);
        }
    }

    protected Label getPageTitle() {
        String service = getPageParameters().get("service").toString();
        return new Label("pageTitle", Model.of(MessageFormat.format(getString("page.title"), service)));
    }
}