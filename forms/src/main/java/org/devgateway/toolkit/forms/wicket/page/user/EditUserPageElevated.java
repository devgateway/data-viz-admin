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
package org.devgateway.toolkit.forms.wicket.page.user;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.breadcrumbs.BreadCrumbPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListUserPage;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/user")
@BreadCrumbPage(parent = ListUserPage.class)
public class EditUserPageElevated extends EditUserPage {
    private static final long serialVersionUID = -5372177614898411737L;

    public EditUserPageElevated(final PageParameters parameters) {
        super(parameters);
    }

    private boolean hasBreadcrumbPanel() {
        return this.getClass().getDeclaredAnnotation(BreadCrumbPage.class) != null;
    }

}
