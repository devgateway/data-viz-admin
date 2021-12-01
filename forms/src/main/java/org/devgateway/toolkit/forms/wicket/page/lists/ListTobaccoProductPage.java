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
package org.devgateway.toolkit.forms.wicket.page.lists;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.TestFormFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.TobaccoProductFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.edit.EditTestFormPage;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditTobaccoProductPage;
import org.devgateway.toolkit.persistence.dao.TestForm;
import org.devgateway.toolkit.persistence.dao.categories.TobaccoProduct;
import org.devgateway.toolkit.persistence.service.RoleService;
import org.devgateway.toolkit.persistence.service.TestFormService;
import org.devgateway.toolkit.persistence.service.category.TobaccoProductService;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath(value = "/listTobaccoProduct")
public class ListTobaccoProductPage extends AbstractListPage<TobaccoProduct> {
    private static final long serialVersionUID = -324298525712620234L;

    @SpringBean
    private TobaccoProductService tobaccoProductService;

    public ListTobaccoProductPage(final PageParameters pageParameters) {
        super(pageParameters);
        this.jpaService = tobaccoProductService;
        this.editPageClass = EditTobaccoProductPage.class;

        columns.add(new TextFilteredBootstrapPropertyColumn<>(new StringResourceModel("label"), "label", "label"));
        columns.add(new PropertyColumn<>(new StringResourceModel("illicit"), "illicit", "illicit"));
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();

        dataProvider.setSort("label", SortOrder.ASCENDING);

        excelForm.setVisibilityAllowed(false);
    }

    @Override
    protected Component getRevisionsLink(final TobaccoProduct entity) {
        return new WebMarkupContainer("revisions").setVisibilityAllowed(false);
    }

    @Override
    public JpaFilterState<TobaccoProduct> newFilterState() {
        return new TobaccoProductFilterState();
    }
}
