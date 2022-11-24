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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.breadcrumbs.BreadCrumbPage;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.TetsimDatasetFilterState;
import org.devgateway.toolkit.forms.wicket.page.DataServicePage;
import org.devgateway.toolkit.forms.wicket.page.edit.dataset.EditTetsimDatasetPage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.devgateway.toolkit.web.util.SettingsUtils;
import org.springframework.util.ObjectUtils;
import org.wicketstuff.annotation.mount.MountPath;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.devgateway.toolkit.forms.WebConstants.PARAM_SERVICE;
import static org.devgateway.toolkit.forms.WebConstants.PARAM_YEAR;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath(value = "/listTetsimDataset")
@BreadCrumbPage(parent = DataServicePage.class, params = {"service"})
public class ListTetsimDatasetPage extends AbstractListPage<TetsimDataset> {
    private static final long serialVersionUID = -324298525712620234L;
    @SpringBean
    protected SettingsUtils settingsUtils;

    TetsimDatasetFilterState filterState;

    protected Select2ChoiceBootstrapFormComponent<Integer> year;
    protected Fragment yearSelectorFragment;
    private Integer selectedYear;
    @SpringBean
    private TetsimDatasetService tetsimDatasetService;
    private Label addButtonError;

    public ListTetsimDatasetPage(final PageParameters pageParameters) {
        super(pageParameters);
        this.jpaService = tetsimDatasetService;
        this.editPageClass = EditTetsimDatasetPage.class;

        String service = pageParameters.get("service").toString();

        columns.clear();

        List<TetsimDataset> datasets = tetsimDatasetService.findAllNotDeletedForService(service);
        List<Integer> years = datasets.stream()
                .map(TetsimDataset::getYear).distinct().sorted()
                .collect(Collectors.toList());
        columns.add(new BootstrapChoiceFilteredPropertyColumn<>(new StringResourceModel("year"), "year", "year",
                new DozerListModel<>(years), "year"));

        List<String> statuses = datasets.stream()
                .map(TetsimDataset::getStatus).distinct().sorted()
                .collect(Collectors.toList());
        columns.add(new BootstrapChoiceFilteredPropertyColumn<>(new StringResourceModel("status"), "status", "status",
                new DozerListModel<>(statuses), "status"));

        columns.add(new PropertyColumn<>(new StringResourceModel("lastModifiedBy"), "lastModifiedBy",
                "lastModifiedBy.get"));
        columns.add(new PropertyColumn<TetsimDataset, String>(new StringResourceModel("lastModifiedDate"),
                "lastModifiedDate",
                "lastModifiedDate.get") {

            @Override
            public IModel<?> getDataModel(final IModel<TetsimDataset> rowModel) {
                IModel<?> model = super.getDataModel(rowModel);
                ZonedDateTime modifiedDate = (ZonedDateTime) model.getObject();
                return Model.of(modifiedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            }
        });


        filterState = new TetsimDatasetFilterState();
        filterState.setService(service);

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        dataProvider.setSort("year", SortOrder.DESCENDING);

        addNewYearSelector();

        editPageLink.setEnabled(false);

        excelForm.setVisibilityAllowed(false);
    }

    private void addNewYearSelector() {
        yearSelectorFragment = new Fragment("bottomPageFragment", "yearSelectorFragment",
                ListTetsimDatasetPage.this);
        replace(yearSelectorFragment);

        Form<Void> form = new Form<>("form");
        yearSelectorFragment.add(form);

        year = new Select2ChoiceBootstrapFormComponent<Integer>("year",
                new GenericChoiceProvider<>(getYearsNewData()), Model.of(selectedYear)) {
            @Override
            public String getPlaceholder() {
                return "Select new year to add";
            }

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                preparePageLinkButton(target);
            }
        };

        year.setVisibilityAllowed(true);
        year.hideLabel();
        form.add(year);

        Label helperTextYear = new Label("helperTextYear");
        form.add(helperTextYear);

        addButtonError = new Label("addButtonError");
        addButtonError.setOutputMarkupId(true);
        addButtonError.setOutputMarkupPlaceholderTag(true);
        addButtonError.setVisibilityAllowed(false);
        form.add(addButtonError);
    }

    protected List<Integer> getYearsNewData() {
        return settingsUtils.getYearsRange();
    }

    protected void preparePageLinkButton(final AjaxRequestTarget target) {
        if (year.getModelObject() == null) {
            addButtonError.setVisibilityAllowed(false);
            editPageLink.setEnabled(false);
            target.add(addButtonError, editPageLink);
            return;
        }

        List<String> errors = getAddButtonErrors();

        addButtonError.setVisibilityAllowed(!errors.isEmpty());
        addButtonError.setDefaultModel(Model.of(String.join("\n", errors)));
        target.add(addButtonError);

        if (!errors.isEmpty()) {
            editPageLink.setEnabled(false);
            target.add(editPageLink);
            return;
        }

        if (!ObjectUtils.isEmpty(year.getModelObject())) {
            editPageLink.setEnabled(true);
            attachYearParameters(editPageLink.getPageParameters(), year.getModelObject());
        } else {
            editPageLink.setEnabled(false);
        }

        target.add(editPageLink);
    }

    @Override
    protected PageParameters getEditPageParameters() {
        PageParameters pageParams = super.getEditPageParameters();
        pageParams.set(PARAM_SERVICE, getPageParameters().get(PARAM_SERVICE).toString());
        return pageParams;
    }

    protected List<String> getAddButtonErrors() {
        long c = countByNonPublished();
        if (c > 0) {
            return Collections.singletonList(getString("existingData"));
        } else {
            return Collections.emptyList();
        }
    }

    protected long countByNonPublished() {
        return tetsimDatasetService.countByNonPublished(year.getModelObject());
    }

    void attachYearParameters(PageParameters pageParameters, Integer year) {
        if (year != null) {
            pageParameters.set(PARAM_YEAR, year);
        }
    }

    @Override
    protected Component getRevisionsLink(final TetsimDataset entity) {
        return new WebMarkupContainer("revisions").setVisibilityAllowed(false);
    }

    @Override
    public JpaFilterState<TetsimDataset> newFilterState() {
        return new TetsimDatasetFilterState();
    }

    protected Label getPageTitle() {
        String service = getPageParameters().get("service").toString();
        return new Label("pageTitle", getPageTitleModel());
    }

    @Override
    protected IModel<String> getBreadcrumbTitleModel() {
        return getPageTitleModel();
    }

    private Model<String> getPageTitleModel() {
        String service = getPageParameters().get("service").toString();
        return Model.of(MessageFormat.format(getString("page.title"), service));
    }

    @Override
    protected void addEditLinkPageParameters(final PageParameters pageParameters) {
        pageParameters.set(WebConstants.PARAM_SERVICE, getPageParameters().get(WebConstants.PARAM_SERVICE));
    }
}
