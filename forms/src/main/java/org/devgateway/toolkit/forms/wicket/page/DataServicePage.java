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
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.BigLinkDefinition;
import org.devgateway.toolkit.forms.wicket.components.BigLinksPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceDimensionsPage;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListCSVDatasetPage;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

import static org.devgateway.toolkit.forms.WebConstants.PARAM_ENTITY;
import static org.devgateway.toolkit.forms.WebConstants.PARAM_SERVICE;

/**
 * @author Viorel Chihai
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class DataServicePage extends BasePage {

    public DataServicePage(final PageParameters parameters) {
        super(parameters);

        List<BigLinkDefinition> links = new ArrayList<>();
        links.add(getEntityLink("datasets", ListCSVDatasetPage.class, FontAwesome5IconType.database_s));
        links.add(getEntityLink("measures", ListServiceDimensionsPage.class, FontAwesome5IconType.ruler_s));
        links.add(getEntityLink("dimensions", ListServiceDimensionsPage.class, FontAwesome5IconType.chart_bar_s));
        links.add(getEntityLink("categories", ListServiceDimensionsPage.class, FontAwesome5IconType.layer_group_s));

        add(new BigLinksPanel("links", Model.ofList(links)));
    }

    private BigLinkDefinition getEntityLink(final String entity, Class<? extends Page> pageClass, IconType iconType) {
        PageParameters pageParams = new PageParameters();
        pageParams.set(PARAM_SERVICE, getPageParameters().get(PARAM_SERVICE).toString());
        pageParams.set(PARAM_ENTITY, entity);

        return new BigLinkDefinition(entity, pageClass, pageParams, iconType);
    }

    protected Label getPageTitle() {
        String service = getPageParameters().get("service").toString();
        return new Label("pageTitle", Model.of(service));
    }
}
