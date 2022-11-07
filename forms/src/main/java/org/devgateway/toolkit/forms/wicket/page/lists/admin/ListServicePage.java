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
package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.forms.wicket.components.LinkTargetBlankPanel;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.persistence.dto.ServiceMetadata;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

@MountPath(value = "/listservices")
public class ListServicePage extends BasePage {

    private static final long serialVersionUID = -6132847935476573446L;

    @SpringBean
    AdminSettingsService adminSettingsService;

    protected AjaxFallbackBootstrapDataTable<ServiceMetadata, String> dataTable;

    protected ServiceMetadataProvider dataProvider;

    protected List<IColumn<ServiceMetadata, String>> columns;

    @SpringBean
    private EurekaClientService eurekaClientService;

    protected int pageRowNo = 0;

    public ListServicePage(final PageParameters pageParameters) {
        super(pageParameters);

        dataProvider = new ServiceMetadataProvider(eurekaClientService);

        columns = new ArrayList<>();
        columns.add(new PropertyColumn<>(new Model<>("Name"), "name", "name"));
        columns.add(new PropertyColumn<>(new Model<>("ID"), "id", "id"));
            columns.add(new PropertyColumn<ServiceMetadata, String>(new Model<>("URL"), "url", "url") {
            @Override
            public void populateItem(final Item<ICellPopulator<ServiceMetadata>> item, final String componentId,
                                     final IModel<ServiceMetadata> rowModel) {
                item.add(new LinkTargetBlankPanel(componentId, new PropertyModel<>(rowModel, "url")));
            }
        });
        columns.add(new PropertyColumn<>(new Model<>("Type"), "type", "type"));
        columns.add(new PropertyColumn<ServiceMetadata, String>(new Model<>("Status"), "status", "status") {
            @Override
            public void populateItem(final Item<ICellPopulator<ServiceMetadata>> item, final String componentId,
                                     final IModel<ServiceMetadata> rowModel) {
                String cssClass = rowModel.getObject().getStatus().equals("UP") ? "label-success" : "label-danger";
                Label label = new Label(componentId, this.getDataModel(rowModel));
                label.add(AttributeAppender.append("class", "label " + cssClass));
                item.add(label);
            }
        });

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        dataTable = new AjaxFallbackBootstrapDataTable<ServiceMetadata, String>(
                "table", columns, dataProvider, getPageSize()) {
            private static final long serialVersionUID = -7263599298497560059L;

            @Override
            protected void onPageChanged() {
                pageRowNo = 0;
            }
        };

        columns.add(new AbstractColumn<ServiceMetadata, String>(new StringResourceModel("actionsColumn", this, null)) {
            private static final long serialVersionUID = -7447601118569862123L;

            @Override
            public void populateItem(final Item<ICellPopulator<ServiceMetadata>> cellItem, final String componentId,
                                     final IModel<ServiceMetadata> model) {
                cellItem.add(getActionPanel(componentId, model));
            }
        });

        add(dataTable);
    }

    protected int getPageSize() {
        return adminSettingsService.get().getPageSize();
    }

    public ActionPanel getActionPanel(final String id, final IModel<ServiceMetadata> model) {
        return new ActionPanel(id, model);
    }

    public class ActionPanel extends GenericPanel<ServiceMetadata> {
        private static final long serialVersionUID = 5821419128121941939L;

        /**
         * @param id
         * @param model
         */
        public ActionPanel(final String id, final IModel<ServiceMetadata> model) {
            super(id, model);

            String serviceName = model.getObject().getName();
            add(getLink("dimensions", serviceName));
            add(getLink("measures", serviceName));
            add(getLink("filters", serviceName));
            add(getLink("categories", serviceName));
        }

        protected Component getLink(final String entity, final String serviceName) {
            PageParameters serviceEntityPageParameters = new PageParameters();
            serviceEntityPageParameters.set(WebConstants.PARAM_SERVICE, serviceName);
            serviceEntityPageParameters.set(WebConstants.PARAM_ENTITY, entity);

            BootstrapBookmarkablePageLink<Void> entityLink = new BootstrapBookmarkablePageLink<>(entity,
                    ListServiceDimensionsPage.class, serviceEntityPageParameters, Buttons.Type.Info);
            entityLink.setIconType(FontAwesome5IconType.clock_s).setSize(Buttons.Size.Small)
                    .setLabel(new StringResourceModel(entity, ListServicePage.this, null));

            return entityLink;
        }
    }
}