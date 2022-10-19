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

import com.google.common.collect.ImmutableList;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.forms.wicket.components.BigLinkDefinition;
import org.devgateway.toolkit.forms.wicket.components.BigLinksPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListCSVDatasetPage;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListTetsimDatasetPage;
import org.devgateway.toolkit.persistence.dto.ServiceMetadata;
import org.devgateway.toolkit.web.util.SettingsUtils;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Viorel Chihai
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class DataPage extends BasePage {

    @SpringBean
    protected SettingsUtils settingsUtils;

    @SpringBean
    private EurekaClientService eurekaClientService;

    public DataPage(final PageParameters parameters) {
        super(parameters);

        List<ServiceMetadata> services = eurekaClientService.findAllWithData();

        List<BigLinkDefinition> links = new ArrayList<>();
        for (ServiceMetadata service : services) {
            PageParameters pageParameters = new PageParameters();
            pageParameters.add("service", service.getName());

            FontAwesome5IconType serviceIcon = getServiceIcon(service.getType());

            links.add(new BigLinkDefinition(service.getId(), DataServicePage.class, pageParameters, serviceIcon) {

                @Override
                public IModel<String> getLabelModel() {
                    return Model.of(service.getName());
                }

                @Override
                public IModel<String> getDescModel() {
                    return Model.of(service.getDescription());
                }
            });
        }

        add(new BigLinksPanel("links", Model.ofList(links)));
        Label noServiceError = new Label("noServiceError", new StringResourceModel("noServiceError", this, null));
        noServiceError.setVisible(links.isEmpty());
        add(noServiceError);
    }

    private FontAwesome5IconType getServiceIcon(final String type) {
        if (type.equals(WebConstants.SERVICE_TETSIM_TYPE)) {
            return FontAwesome5IconType.list_alt_r;
        } else if (type.equals(WebConstants.SERVICE_DATA_TYPE)) {
            return FontAwesome5IconType.file_csv_s;
        }

        throw new IllegalArgumentException("Unknown service type: " + type);
    }

    protected Label getPageTitle() {
        return new Label("pageTitle", new StringResourceModel("page.title", this,
                Model.of(settingsUtils.getSetting())));
    }

}
