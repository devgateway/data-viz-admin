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
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.page.edit.admin.EditServicePage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.ServiceMetadata;
import org.devgateway.toolkit.persistence.service.ServiceMetadataService;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/listservices")
public class ListServicePage extends AbstractListPage<ServiceMetadata> {

    private static final long serialVersionUID = -6132847935476573446L;

    @SpringBean
    private ServiceMetadataService serviceMetadataService;

    public ListServicePage(final PageParameters pageParameters) {
        super(pageParameters);

        this.jpaService = serviceMetadataService;

        this.editPageClass = EditServicePage.class;
        columns.add(new PropertyColumn<>(new Model<>("Name"), "name", "name"));
        columns.add(new PropertyColumn<>(new Model<>("Description"), "description", "description"));
        columns.add(new PropertyColumn<>(new Model<>("URL"), "url", "url"));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        excelForm.setVisibilityAllowed(false);
    }
}