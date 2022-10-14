package org.devgateway.toolkit.forms.wicket.page.edit.admin;

import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.service.admin.ServiceMeasureService;
import org.devgateway.toolkit.forms.validators.UniqueLanguageTranslationValidator;
import org.devgateway.toolkit.forms.wicket.components.form.ColorPickerBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.admin.panel.ListViewTextTranslationSectionPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceMeasuresPage;
import org.devgateway.toolkit.persistence.dto.ServiceMeasure;
import org.springframework.beans.factory.annotation.Autowired;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/editServiceMeasure")
public class EditServiceMeasurePage extends AbstractEditServiceEntityPage<ServiceMeasure> {
    private static final long serialVersionUID = -9013029067860834250L;

    @SpringBean
    private ServiceMeasureService serviceMeasureService;

    public EditServiceMeasurePage(final PageParameters parameters) {
        super(parameters);
        this.listPageClass = ListServiceMeasuresPage.class;
        this.entityService = serviceMeasureService;
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

        final TextFieldBootstrapFormComponent<String> expression = new TextFieldBootstrapFormComponent<>("expression");
        expression.setEnabled(false);
        editForm.add(expression);

        ColorPickerBootstrapFormComponent colorPicker = new ColorPickerBootstrapFormComponent("color",
                new PropertyModel<>(editForm.getModelObject(), "categoryStyle.color"));
        editForm.add(colorPicker);

        final TextFieldBootstrapFormComponent<String> className = new TextFieldBootstrapFormComponent<>("className",
                new PropertyModel<>(editForm.getModelObject(), "categoryStyle.className"));
        editForm.add(className);

        ColorPickerBootstrapFormComponent textColorPicker = new ColorPickerBootstrapFormComponent("textColor",
                new PropertyModel<>(editForm.getModelObject(), "categoryStyle.textColor"));
        editForm.add(textColorPicker);

        ColorPickerBootstrapFormComponent bckColorPicker = new ColorPickerBootstrapFormComponent("backgroundColor",
                new PropertyModel<>(editForm.getModelObject(), "categoryStyle.backgroundColor"));
        editForm.add(bckColorPicker);

        editForm.add(new ListViewTextTranslationSectionPanel("labels"));
        editForm.add(new UniqueLanguageTranslationValidator());
    }

}
