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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.service.DatasetClientService;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.persistence.dto.ServiceMetadataDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.wicketstuff.annotation.mount.MountPath;
import org.wicketstuff.egrid.EditableGrid;
import org.wicketstuff.egrid.column.RequiredEditableTextFieldColumn;
import org.wicketstuff.egrid.provider.EditableListDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mpostelnicu
 */
@MountPath(value = "/dimensions")
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
public class ListServiceDimensionsPage extends BasePage {

    private static final long serialVersionUID = 1302443729452089592L;

    private EditableGrid editableGrid;

    private String serviceName;

    @SpringBean
    private DatasetClientService datasetClientService;

    /**
     * @param parameters
     */
    public ListServiceDimensionsPage(final PageParameters parameters) {
        super(parameters);

        this.serviceName = getPageParameters().get(WebConstants.PARAM_SERVICE).toString();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        editableGrid = new EditableGrid<ServiceMetadataDimension, String>("grid", getColumns(),
                new EditableListDataProvider(getObjectList()), 5, ServiceMetadataDimension.class) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(feedbackPanel);
            }

            @Override
            protected void onCancel(AjaxRequestTarget target) {
                target.add(feedbackPanel);
            }

            @Override
            protected void onDelete(AjaxRequestTarget target, IModel<ServiceMetadataDimension> rowModel) {
                target.add(feedbackPanel);
            }

            @Override
            protected void onSave(AjaxRequestTarget target, IModel<ServiceMetadataDimension> rowModel) {
                target.add(feedbackPanel);
            }

            @Override
            protected boolean displayAddFeature() {
                return false;
            }
        };
        editableGrid.setTableCss("dataview table");
        add(editableGrid);
    }

    private List<ServiceMetadataDimension> getObjectList() {
        return datasetClientService.getDimensions(serviceName);
    }

    private List<PropertyColumn<ServiceMetadataDimension, String>> getColumns() {
        List<PropertyColumn<ServiceMetadataDimension, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<>(new Model("Name"), "code"));
        columns.add(new RequiredEditableTextFieldColumn(new Model("Label"), "value"));
        return columns;
    }
}
