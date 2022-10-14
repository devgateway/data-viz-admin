package org.devgateway.toolkit.forms.wicket.page.edit.admin;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.service.admin.ServiceDimensionService;
import org.devgateway.toolkit.forms.validators.UniqueLanguageTranslationValidator;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.admin.panel.ListViewTextTranslationSectionPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceDimensionsPage;
import org.devgateway.toolkit.persistence.dto.ServiceDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/editServiceDimension")
public class EditServiceDimensionPage extends AbstractEditServiceEntityPage<ServiceDimension> {
    private static final long serialVersionUID = -1594571319284288551L;

    @SpringBean
    private ServiceDimensionService serviceDimensionService;

    public EditServiceDimensionPage(final PageParameters parameters) {
        super(parameters);
        this.listPageClass = ListServiceDimensionsPage.class;
        this.entityService = serviceDimensionService;
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

        final TextFieldBootstrapFormComponent<Integer> position = new TextFieldBootstrapFormComponent<>("position");
        position.getField().setRequired(true);
        editForm.add(position);

        editForm.add(new ListViewTextTranslationSectionPanel("labels"));
        editForm.add(new UniqueLanguageTranslationValidator());
    }

}
