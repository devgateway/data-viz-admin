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
package org.devgateway.toolkit.forms.wicket.page.lists.dataset;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.TetsimDatasetFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.dataset.EditTetsimDatasetPage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath(value = "/listTetsimDataset")
public class ListTetsimDatasetPage extends AbstractListPage<TetsimDataset> {
    private static final long serialVersionUID = -324298525712620234L;

    @SpringBean
    private TetsimDatasetService tetsimDatasetService;

    public ListTetsimDatasetPage(final PageParameters pageParameters) {
        super(pageParameters);
        this.jpaService = tetsimDatasetService;
        this.editPageClass = EditTetsimDatasetPage.class;

        columns.clear();

        columns.add(new PropertyColumn<>(new StringResourceModel("year"), "year", "year"));
        columns.add(new PropertyColumn<>(new StringResourceModel("lastModifiedBy"), "lastModifiedBy",
                "lastModifiedBy.get"));
        columns.add(new PropertyColumn<TetsimDataset, String>(new StringResourceModel("lastModifiedDate"),
                "lastModifiedDate",
                "lastModifiedDate.get") {

            @Override
            public IModel<?> getDataModel(final IModel<TetsimDataset> rowModel) {
                IModel<?> model = super.getDataModel(rowModel);
                ZonedDateTime modifiedDate = (ZonedDateTime) model.getObject();
                return  Model.of(modifiedDate.format(DateTimeFormatter.ofPattern("dd/MM/yy")));
            }
        });
        columns.add(new PropertyColumn<>(new StringResourceModel("status"), "status", "status"));

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        dataProvider.setSort("year", SortOrder.ASCENDING);

        excelForm.setVisibilityAllowed(false);
    }

    @Override
    protected Component getRevisionsLink(final TetsimDataset entity) {
        return new WebMarkupContainer("revisions").setVisibilityAllowed(false);
    }

    @Override
    public JpaFilterState<TetsimDataset> newFilterState() {
        return new TetsimDatasetFilterState();
    }
}
