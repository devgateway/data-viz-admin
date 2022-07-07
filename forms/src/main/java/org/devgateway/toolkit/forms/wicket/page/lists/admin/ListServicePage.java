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

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.forms.wicket.components.LinkTargetBlankPanel;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.persistence.dto.ServiceMetadata;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

import static org.devgateway.toolkit.forms.WebConstants.PAGE_SIZE;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/listservices")
public class ListServicePage extends BasePage {

    private static final long serialVersionUID = -6132847935476573446L;

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
        add(dataTable);
    }

    protected int getPageSize() {
        return PAGE_SIZE;
    }
}