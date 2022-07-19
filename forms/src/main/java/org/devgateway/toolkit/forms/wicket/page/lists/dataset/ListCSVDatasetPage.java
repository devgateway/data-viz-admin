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
import org.devgateway.toolkit.forms.wicket.components.table.filter.CSVDatasetFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.dataset.EditCSVDatasetPage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.service.data.CSVDatasetService;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath(value = "/listCSVDataset")
public class ListCSVDatasetPage extends AbstractListPage<CSVDataset> {
    private static final long serialVersionUID = -7425220174797515101L;

    @SpringBean
    private CSVDatasetService datasetService;

    public ListCSVDatasetPage(final PageParameters pageParameters) {
        super(pageParameters);
        this.jpaService = datasetService;
        this.editPageClass = EditCSVDatasetPage.class;

        columns.clear();

        columns.add(new PropertyColumn<>(new StringResourceModel("year"), "year", "year"));
        columns.add(new PropertyColumn<>(new StringResourceModel("lastModifiedBy"), "lastModifiedBy",
                "lastModifiedBy.get"));
        columns.add(new PropertyColumn<CSVDataset, String>(new StringResourceModel("lastModifiedDate"),
                "lastModifiedDate",
                "lastModifiedDate.get") {

            @Override
            public IModel<?> getDataModel(final IModel<CSVDataset> rowModel) {
                IModel<?> model = super.getDataModel(rowModel);
                ZonedDateTime modifiedDate = (ZonedDateTime) model.getObject();
                return Model.of(modifiedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            }
        });
        columns.add(new PropertyColumn<>(new StringResourceModel("destinationService"), "destinationService", "destinationService"));
        columns.add(new PropertyColumn<>(new StringResourceModel("status"), "status", "status"));

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        dataProvider.setSort("year", SortOrder.DESCENDING);

        excelForm.setVisibilityAllowed(false);
    }

    @Override
    protected Component getRevisionsLink(final CSVDataset entity) {
        return new WebMarkupContainer("revisions").setVisibilityAllowed(false);
    }

    @Override
    public JpaFilterState<CSVDataset> newFilterState() {
        return new CSVDatasetFilterState();
    }
}
