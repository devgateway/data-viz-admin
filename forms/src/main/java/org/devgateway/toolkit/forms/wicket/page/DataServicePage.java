/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.forms.wicket.components.BigLinkDefinition;
import org.devgateway.toolkit.forms.wicket.components.BigLinksPanel;
import org.devgateway.toolkit.forms.wicket.components.breadcrumbs.BreadCrumbPage;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceCategoriesPage;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceDimensionsPage;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceFiltersPage;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceMeasuresPage;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListCSVDatasetPage;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListTetsimDatasetPage;
import org.devgateway.toolkit.persistence.dto.ServiceMetadata;
import org.devgateway.toolkit.web.util.SettingsUtils;
import org.wicketstuff.annotation.mount.MountPath;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType.chart_bar_s;
import static de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType.database_s;
import static de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType.filter_s;
import static de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType.layer_group_s;
import static de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType.ruler_s;
import static org.devgateway.toolkit.forms.WebConstants.PARAM_SERVICE;
import static org.devgateway.toolkit.forms.WebConstants.SERVICE_DATA_TYPE;
import static org.devgateway.toolkit.forms.WebConstants.SERVICE_TETSIM_TYPE;

/**
 * @author Viorel Chihai
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
@BreadCrumbPage(parent = DataPage.class, params = {"service"})
public class DataServicePage extends BasePage {

    @SpringBean
    protected SettingsUtils settingsUtils;

    @SpringBean
    private EurekaClientService eurekaClientService;

    public DataServicePage(final PageParameters parameters) {
        super(parameters);

        ServiceMetadata serviceMetadata = eurekaClientService.findByName(parameters.get(PARAM_SERVICE).toString());

        List<BigLinkDefinition> links = new ArrayList<>();
        links.add(getEntityLink("datasets", getServicePageClass(serviceMetadata), database_s));
        links.add(getEntityLink("measures", ListServiceMeasuresPage.class, ruler_s));
        links.add(getEntityLink("dimensions", ListServiceDimensionsPage.class, chart_bar_s));
        links.add(getEntityLink("categories", ListServiceCategoriesPage.class, layer_group_s));
        links.add(getEntityLink("filters", ListServiceFiltersPage.class, filter_s));

        add(new BigLinksPanel("links", Model.ofList(links)));
    }

    private BigLinkDefinition getEntityLink(final String entity, Class<? extends Page> pageClass, IconType iconType) {
        PageParameters pageParams = new PageParameters();
        pageParams.set(PARAM_SERVICE, getPageParameters().get(PARAM_SERVICE).toString());

        return new BigLinkDefinition(entity, pageClass, pageParams, iconType);
    }

    private Class<? extends BasePage> getServicePageClass(final ServiceMetadata serviceMetadata) {
        if (serviceMetadata.getType().equals(SERVICE_TETSIM_TYPE) || serviceMetadata.getName().equalsIgnoreCase(SERVICE_TETSIM_TYPE)) {
            return ListTetsimDatasetPage.class;
        } else if (serviceMetadata.getType().equals(SERVICE_DATA_TYPE)) {
            return ListCSVDatasetPage.class;
        }

        throw new IllegalArgumentException("Unknown service type: " + serviceMetadata.getType());
    }

    protected Label getPageTitle() {
        String service = getPageParameters().get("service").toString();
        String countryName = settingsUtils.getSetting().getCountryName();

        return new Label("pageTitle",
                Model.of(MessageFormat.format(getString("page.title"), countryName, service)));

    }

    protected Model<String> getBreadcrumbTitleModel() {
        String service = getPageParameters().get("service").toString();
        return Model.of(MessageFormat.format(getString("breadcrumb.title"), service));
    }
}
