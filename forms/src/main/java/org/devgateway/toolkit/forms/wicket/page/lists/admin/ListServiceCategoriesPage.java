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

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.filter.BootstrapTextFilteredPropertyColumn;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.service.admin.ServiceCategoryService;
import org.devgateway.toolkit.forms.wicket.components.breadcrumbs.BreadCrumbPage;
import org.devgateway.toolkit.forms.wicket.components.table.filter.ServiceCategoryFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.ServiceEntityFilterState;
import org.devgateway.toolkit.forms.wicket.page.DataServicePage;
import org.devgateway.toolkit.forms.wicket.page.edit.admin.EditServiceCategoryPage;
import org.devgateway.toolkit.persistence.dto.ServiceCategory;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;

@MountPath(value = "/categories")
@BreadCrumbPage(parent = DataServicePage.class, hasServiceParam = true)
public class ListServiceCategoriesPage extends AbstractListServiceEntityPage<ServiceCategory> {

    private static final long serialVersionUID = -6132847935476573446L;

    @SpringBean
    private ServiceCategoryService serviceCategoryService;

    public ListServiceCategoriesPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.serviceEntityService = serviceCategoryService;
        this.editPageClass = EditServiceCategoryPage.class;

        String service = pageParameters.get(WebConstants.PARAM_SERVICE).toString();
        dataProvider = new ServiceCategoryProvider(serviceCategoryService, service);

        columns = new ArrayList<>();
        columns.add(new BootstrapTextFilteredPropertyColumn<>(new Model<>("Value"), "code", "code", "code"));
        columns.add(new BootstrapTextFilteredPropertyColumn<>(new Model<>("System Label"), "value", "value", "value"));
        columns.add(new BootstrapTextFilteredPropertyColumn<>(new Model<>("Type"), "type", "type", "type"));
        columns.add(new PropertyColumn<>(new Model<>("Position"), "position", "position"));
        columns.add(new PropertyColumn<ServiceCategory, String>(new Model<>("Color"), null,
                "categoryStyle.color") {
            @Override
            public void populateItem(final Item<ICellPopulator<ServiceCategory>> item, final String componentId, final IModel<ServiceCategory> rowModel) {
                String color = rowModel.getObject().getCategoryStyle() == null ? "" : rowModel.getObject().getCategoryStyle().getColor();
                item.add(new Label(componentId, "")
                        .add(new AttributeModifier("style", "background-color: " + color))
                        .add(new CssClassNameAppender("color-box"))
                );
            }
        });
    }

    @Override
    public ServiceEntityFilterState<ServiceCategory> newFilterState() {
        return new ServiceCategoryFilterState();
    }

}