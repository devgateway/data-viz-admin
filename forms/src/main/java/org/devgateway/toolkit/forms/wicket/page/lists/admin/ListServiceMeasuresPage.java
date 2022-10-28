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
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.service.admin.ServiceMeasureService;
import org.devgateway.toolkit.forms.wicket.page.edit.admin.EditServiceMeasurePage;
import org.devgateway.toolkit.persistence.dto.ServiceMeasure;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/measures")
public class ListServiceMeasuresPage extends AbstractListServiceEntityPage<ServiceMeasure> {

    private static final long serialVersionUID = -6132847935476573446L;

    @SpringBean
    private ServiceMeasureService serviceMeasureService;

    public ListServiceMeasuresPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.serviceEntityService = serviceMeasureService;
        this.editPageClass = EditServiceMeasurePage.class;

        columns = new ArrayList<>();
        columns.add(new PropertyColumn<>(new Model<>("Value"), "code", "code"));
        columns.add(new PropertyColumn<>(new Model<>("Label"), "value", "value"));
        columns.add(new PropertyColumn<>(new Model<>("Position"), "position", "position"));
        columns.add(new PropertyColumn<ServiceMeasure, String>(new Model<>("Color"), "categoryStyle.color",
                "categoryStyle.color") {
            @Override
            public void populateItem(final Item<ICellPopulator<ServiceMeasure>> item, final String componentId, final IModel<ServiceMeasure> rowModel) {
                item.add(new Label(componentId, "")
                        .add(new AttributeModifier("style", "background-color: " + rowModel.getObject().getCategoryStyle().getColor()))
                        .add(new CssClassNameAppender("color-box"))
                );
            }
        });
    }
}