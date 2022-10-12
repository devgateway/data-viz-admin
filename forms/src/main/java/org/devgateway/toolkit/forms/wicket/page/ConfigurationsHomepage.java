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
import org.devgateway.toolkit.forms.wicket.page.lists.ListGroupPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListTobaccoProductPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListUserPage;
import org.devgateway.toolkit.web.util.SettingsUtils;

import java.util.List;

/**
 * @author mpostelnicu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class ConfigurationsHomepage extends BasePage {

    @SpringBean
    protected SettingsUtils settingsUtils;

    private static final List<BigLinkDefinition> LINKS = new ImmutableList.Builder<BigLinkDefinition>()
            .add(new BigLinkDefinition("users", ListUserPage.class, FontAwesome5IconType.users_s))
            .add(new BigLinkDefinition("groups", ListGroupPage.class, FontAwesome5IconType.tags_s))
            .add(new BigLinkDefinition("tobaccoProducts", ListTobaccoProductPage.class, FontAwesome5IconType.smoking_s))
            .add(new BigLinkDefinition("settings", EditAdminSettingsPage.class, FontAwesome5IconType.cogs_s))
            .build();

    /**
     * @param parameters
     */
    public ConfigurationsHomepage(final PageParameters parameters) {
        super(parameters);

        add(new BigLinksPanel("links", Model.ofList(LINKS)));
    }

    protected Label getPageTitle() {
        return new Label("pageTitle", new StringResourceModel("page.title", this,
                Model.of(settingsUtils.getSetting())));
    }
}
