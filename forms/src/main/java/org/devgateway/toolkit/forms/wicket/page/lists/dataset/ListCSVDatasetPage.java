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

import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.filter.BootstrapChoiceFilteredPropertyColumn;
import nl.dries.wicket.hibernate.dozer.DozerListModel;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.breadcrumbs.BreadCrumbPage;
import org.devgateway.toolkit.forms.wicket.components.table.filter.CSVDatasetFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.page.DataServicePage;
import org.devgateway.toolkit.forms.wicket.page.edit.dataset.EditCSVDatasetPage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.service.data.CSVDatasetService;
import org.wicketstuff.annotation.mount.MountPath;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.devgateway.toolkit.forms.WebConstants.PARAM_SERVICE;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath(value = "/listCSVDataset")
@BreadCrumbPage(parent = DataServicePage.class, hasServiceParam = true)
public class ListCSVDatasetPage extends AbstractListPage<CSVDataset> {
    private static final long serialVersionUID = -7425220174797515101L;

    @SpringBean
    private CSVDatasetService datasetService;

    CSVDatasetFilterState filterState;

    public ListCSVDatasetPage(final PageParameters pageParameters) {
        super(pageParameters);

        String service = pageParameters.get(PARAM_SERVICE).toString();

        this.jpaService = datasetService;
        this.editPageClass = EditCSVDatasetPage.class;

        columns.clear();

        List<CSVDataset> datasets = datasetService.findAllNotDeletedForService(service);
        List<Integer> years = datasets.stream()
                .map(CSVDataset::getYear).distinct().sorted()
                .collect(Collectors.toList());
        columns.add(new BootstrapChoiceFilteredPropertyColumn<>(new StringResourceModel("year"), "year", "year",
                new DozerListModel<>(years), "year"));

        List<String> statuses = datasets.stream()
                .map(CSVDataset::getStatus).distinct().sorted()
                .collect(Collectors.toList());
        columns.add(new BootstrapChoiceFilteredPropertyColumn<>(new StringResourceModel("status"), "status", "status",
                new DozerListModel<>(statuses), "status"));

        columns.add(new PropertyColumn<>(new StringResourceModel("description"), "description"));
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


        filterState = new CSVDatasetFilterState();
        filterState.setService(service);

    }

    @Override
    protected PageParameters getEditPageParameters() {
        PageParameters pageParams = super.getEditPageParameters();
        pageParams.set(PARAM_SERVICE, getPageParameters().get(PARAM_SERVICE).toString());
        return pageParams;
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
        return filterState;
    }

    protected Label getPageTitle() {
        return new Label("pageTitle", getPageTitleModel());
    }

    @Override
    protected IModel<String> getBreadcrumbTitleModel() {
        return Model.of(MessageFormat.format(getString("breadcrumb.title"), getServiceLabel()));
    }

    private Model<String> getPageTitleModel() {
        return Model.of(MessageFormat.format(getString("page.title"), getServiceLabel()));
    }

    @Override
    protected void addEditLinkPageParameters(final PageParameters pageParameters) {
        pageParameters.set(PARAM_SERVICE, getPageParameters().get(PARAM_SERVICE));
    }

}
