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
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.service.DatasetClientService;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.edit.admin.EditServiceMeasurePage;
import org.devgateway.toolkit.persistence.dto.ServiceMeasure;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

import static org.devgateway.toolkit.forms.WebConstants.PAGE_SIZE;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/measures")
public class ListServiceMeasuresPage extends BasePage {

    private static final long serialVersionUID = -6132847935476573446L;

    protected AjaxFallbackBootstrapDataTable<ServiceMeasure, String> dataTable;

    protected ServiceMeasureProvider dataProvider;

    protected List<IColumn<ServiceMeasure, String>> columns;

    @SpringBean
    private DatasetClientService datasetClientService;

    protected int pageRowNo = 0;

    public ListServiceMeasuresPage(final PageParameters pageParameters) {
        super(pageParameters);

        String service = pageParameters.get(WebConstants.PARAM_SERVICE).toString();
        dataProvider = new ServiceMeasureProvider(datasetClientService, service);

        columns = new ArrayList<>();
        columns.add(new PropertyColumn<>(new Model<>("Code"), "code", "code"));
        columns.add(new PropertyColumn<>(new Model<>("Value"), "value", "value"));
        columns.add(new PropertyColumn<>(new Model<>("Position"), "position", "position"));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        dataTable = new AjaxFallbackBootstrapDataTable<ServiceMeasure, String>(
                "table", columns, dataProvider, getPageSize()) {
            private static final long serialVersionUID = -7263599298497560059L;

            @Override
            protected void onPageChanged() {
                pageRowNo = 0;
            }
        };

        columns.add(new AbstractColumn<ServiceMeasure, String>(new StringResourceModel("actionsColumn", this, null)) {
            private static final long serialVersionUID = -7447601118569862123L;

            @Override
            public void populateItem(final Item<ICellPopulator<ServiceMeasure>> cellItem, final String componentId,
                                     final IModel<ServiceMeasure> model) {
                cellItem.add(getActionPanel(componentId, model));
            }
        });

        add(dataTable);
    }

    protected int getPageSize() {
        return PAGE_SIZE;
    }

    public ActionPanel getActionPanel(final String id, final IModel<ServiceMeasure> model) {
        return new ActionPanel(id, model);
    }

    public class ActionPanel extends GenericPanel<ServiceMeasure> {
        private static final long serialVersionUID = -4547406060736744071L;

        protected BootstrapBookmarkablePageLink<ServiceMeasure> editItemPageLink;

        /**
         * @param id
         * @param model
         */
        public ActionPanel(final String id, final IModel<ServiceMeasure> model) {
            super(id, model);

            final PageParameters pageParameters = new PageParameters();

            @SuppressWarnings("unchecked")
            ServiceMeasure entity = (ServiceMeasure) ActionPanel.this.getDefaultModelObject();
            if (entity != null) {
                pageParameters.set(WebConstants.PARAM_ID, entity.getId());
            }
            pageParameters.set(WebConstants.PARAM_SERVICE, getPageParameters().get(WebConstants.PARAM_SERVICE));

            editItemPageLink =
                    new BootstrapBookmarkablePageLink<>("edit", EditServiceMeasurePage.class, pageParameters, Buttons.Type.Info);
            editItemPageLink.setIconType(FontAwesome5IconType.edit_s).setSize(Buttons.Size.Small)
                    .setLabel(new StringResourceModel("edit", this, null));
            add(editItemPageLink);
        }
    }
}