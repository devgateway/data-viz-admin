package org.devgateway.toolkit.forms.wicket.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.breadcrumbs.BreadCrumbPage;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.AdminSettings;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Arrays;
import java.util.List;

/**
 * @author idobre
 * @since 6/22/16
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/adminsettings")
@BreadCrumbPage(parent = ConfigurationsHomepage.class)
public class EditAdminSettingsPage extends AbstractEditPage<AdminSettings> {

    private static final long serialVersionUID = 5742724046825803877L;

    private CheckBoxToggleBootstrapFormComponent rebootServer;

    private TextFieldBootstrapFormComponent<Object> autosaveTime;

    private Select2ChoiceBootstrapFormComponent<Integer> pageSize;

    @SpringBean
    private AdminSettingsService adminSettingsService;

    public EditAdminSettingsPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = adminSettingsService;
        this.listPageClass = ConfigurationsHomepage.class;

        if (entityId == null) {
            final List<AdminSettings> listSettings = adminSettingsService.findAll();
            // just keep 1 entry for settings
            if (listSettings.size() == 1) {
                entityId = listSettings.get(0).getId();
            }
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        editForm.add(new Label("systemTitle", new StringResourceModel("systemTitle", this, null)));

        rebootServer = new CheckBoxToggleBootstrapFormComponent("rebootServer");
        editForm.add(rebootServer);

        editForm.add(new Label("appSettingsTitle", new StringResourceModel("appSettingsTitle", this, null)));

        TextFieldBootstrapFormComponent<String> tetsimCurrency = new TextFieldBootstrapFormComponent<>("tetsimCurrency");
        tetsimCurrency.required();
        tetsimCurrency.getBorder().add(AttributeModifier.append("class", "required-field"));
        editForm.add(tetsimCurrency);

        editForm.add(new TextFieldBootstrapFormComponent<>("countryName"));

        autosaveTime = new TextFieldBootstrapFormComponent<>("autosaveTime");
        autosaveTime.integer().required();
        autosaveTime.getBorder().add(AttributeModifier.append("class", "required-field"));
        autosaveTime.getField().add(RangeValidator.range(0, 60));
        autosaveTime.setShowTooltip(true);
        editForm.add(autosaveTime);

        pageSize = new Select2ChoiceBootstrapFormComponent<>("pageSize",
                new GenericChoiceProvider<>(getPageSizeRange()));
        pageSize.required();
        pageSize.getBorder().add(AttributeModifier.append("class", "required-field"));
        editForm.add(pageSize);

    }

    public List<Integer> getPageSizeRange() {
        return Arrays.asList(5, 10, 15, 20, 25, 50, 100);
    }
}
