package org.devgateway.toolkit.forms.wicket.page.edit.admin;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.service.admin.ServiceFilterService;
import org.devgateway.toolkit.forms.validators.UniqueLanguageTranslationValidator;
import org.devgateway.toolkit.forms.wicket.components.breadcrumbs.BreadCrumbPage;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.admin.panel.ListViewTextTranslationSectionPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceFiltersPage;
import org.devgateway.toolkit.persistence.dto.ServiceFilter;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/editServiceFilter")
@BreadCrumbPage(parent = ListServiceFiltersPage.class, hasServiceParam = true)
public class EditServiceFilterPage extends AbstractEditServiceEntityPage<ServiceFilter> {
    private static final long serialVersionUID = -8409084805556720923L;

    @SpringBean
    private ServiceFilterService serviceFilterService;

    public EditServiceFilterPage(final PageParameters parameters) {
        super(parameters);
        this.listPageClass = ListServiceFiltersPage.class;
        this.entityService = serviceFilterService;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final TextFieldBootstrapFormComponent<String> code = new TextFieldBootstrapFormComponent<>("code");
        code.setEnabled(false);
        editForm.add(code);

        final TextFieldBootstrapFormComponent<String> value = new TextFieldBootstrapFormComponent<>("value");
        value.getField().setRequired(true);
        editForm.add(value);

        final TextFieldBootstrapFormComponent<String> fieldType = new TextFieldBootstrapFormComponent<>("fieldType");
        fieldType.setEnabled(false);
        editForm.add(fieldType);

        editForm.add(new ListViewTextTranslationSectionPanel("labels"));
        editForm.add(new UniqueLanguageTranslationValidator());
    }

}
