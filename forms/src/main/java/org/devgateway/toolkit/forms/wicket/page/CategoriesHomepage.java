package org.devgateway.toolkit.forms.wicket.page;

import com.google.common.collect.ImmutableList;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.BigLinkDefinition;
import org.devgateway.toolkit.forms.wicket.components.BigLinksPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.ListTobaccoProductPage;
import org.devgateway.toolkit.web.util.SettingsUtils;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

/**
 * @author Viorel Chihai
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class CategoriesHomepage extends BasePage {

    @SpringBean
    protected SettingsUtils settingsUtils;

    private static final List<BigLinkDefinition> LINKS = new ImmutableList.Builder<BigLinkDefinition>()
            .add(new BigLinkDefinition("tobaccoProducts", ListTobaccoProductPage.class,
                    FontAwesome5IconType.smoking_s))
            .build();

    public CategoriesHomepage(final PageParameters parameters) {
        super(parameters);

        add(new BigLinksPanel("links", Model.ofList(LINKS)));
    }

    @Override
    protected Label getPageTitle() {
        return new Label("pageTitle", new StringResourceModel("page.title", this,
                Model.of(settingsUtils.getSetting())));
    }
}
