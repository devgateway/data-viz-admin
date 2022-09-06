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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.BigLinkDefinition;
import org.devgateway.toolkit.forms.wicket.components.BigLinksPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListCSVDatasetPage;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListTetsimDatasetPage;
import org.devgateway.toolkit.web.util.SettingsUtils;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

/**
 * @author Viorel Chihai
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class DatasetsHomepage extends BasePage {

    @SpringBean
    protected SettingsUtils settingsUtils;

    private static final List<BigLinkDefinition> LINKS = new ImmutableList.Builder<BigLinkDefinition>()
            .add(new BigLinkDefinition("tetsimDataset", ListTetsimDatasetPage.class,
                    FontAwesome5IconType.percentage_s))
            .add(new BigLinkDefinition("csvDataset", ListCSVDatasetPage.class,
                    FontAwesome5IconType.file_csv_s))
            .build();

    public DatasetsHomepage(final PageParameters parameters) {
        super(parameters);

        add(new BigLinksPanel("links", Model.ofList(LINKS)));
    }

    protected Label getPageTitle() {
        return new Label("pageTitle", new StringResourceModel("page.title", this,
                Model.of(settingsUtils.getSetting())));
    }
}
