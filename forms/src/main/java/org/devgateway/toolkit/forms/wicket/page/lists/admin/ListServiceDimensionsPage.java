/*******************************************************************************
 * Copyright (c) 2023 Development Gateway, Inc and others.
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

import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.filter.BootstrapTextFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.service.admin.ServiceDimensionService;
import org.devgateway.toolkit.forms.wicket.components.breadcrumbs.BreadCrumbPage;
import org.devgateway.toolkit.forms.wicket.components.table.filter.ServiceDimensionFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.ServiceEntityFilterState;
import org.devgateway.toolkit.forms.wicket.page.DataServicePage;
import org.devgateway.toolkit.forms.wicket.page.edit.admin.EditServiceDimensionPage;
import org.devgateway.toolkit.persistence.dto.ServiceDimension;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;

@MountPath(value = "/dimensions")
@BreadCrumbPage(parent = DataServicePage.class, hasServiceParam = true)
public class ListServiceDimensionsPage extends AbstractListServiceEntityPage<ServiceDimension> {

    private static final long serialVersionUID = -6132847935476573446L;

    @SpringBean
    private ServiceDimensionService serviceDimensionService;

    public ListServiceDimensionsPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.serviceEntityService = serviceDimensionService;
        this.editPageClass = EditServiceDimensionPage.class;

        String service = pageParameters.get(WebConstants.PARAM_SERVICE).toString();
        dataProvider = new ServiceDimensionProvider(serviceDimensionService, service);

        columns = new ArrayList<>();
        columns.add(new BootstrapTextFilteredPropertyColumn<>(new Model<>("Value"), "code", "code", "code"));
        columns.add(new BootstrapTextFilteredPropertyColumn<>(new Model<>("System Label"), "value", "value", "value"));
        columns.add(new PropertyColumn<ServiceDimension, String>(new Model<>("Label Translations"), null, "labels") {
            @Override
            public void populateItem(final Item<ICellPopulator<ServiceDimension>> item, final String componentId, final IModel<ServiceDimension> rowModel) {
                item.add(getLableTranslations(componentId, rowModel));
            }
        });
        columns.add(new PropertyColumn<>(new Model<>("Position"), "position", "position"));
    }

    @Override
    public ServiceEntityFilterState<ServiceDimension> newFilterState() {
        return new ServiceDimensionFilterState();
    }
}