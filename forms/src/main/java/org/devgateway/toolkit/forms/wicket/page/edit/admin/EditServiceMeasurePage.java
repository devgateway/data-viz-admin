package org.devgateway.toolkit.forms.wicket.page.edit.admin;

import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.service.admin.ServiceMeasureService;
import org.devgateway.toolkit.forms.validators.UniqueLanguageTranslationValidator;
import org.devgateway.toolkit.forms.wicket.components.breadcrumbs.BreadCrumbPage;
import org.devgateway.toolkit.forms.wicket.components.form.ColorPickerBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.admin.panel.ListViewTextTranslationSectionPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceMeasuresPage;
import org.devgateway.toolkit.persistence.dto.ServiceMeasure;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/editServiceMeasure")
@BreadCrumbPage(parent = ListServiceMeasuresPage.class, hasServiceParam = true)
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

        final TextFieldBootstrapFormComponent<String> value = new TextFieldBootstrapFormComponent<>("code");
        value.setEnabled(false);
        editForm.add(value);

        final TextFieldBootstrapFormComponent<String> label = new TextFieldBootstrapFormComponent<>("value");
        label.getField().setRequired(true);
        editForm.add(label);

        final TextFieldBootstrapFormComponent<Integer> parent = new TextFieldBootstrapFormComponent<>("parent");
        parent.setEnabled(false);
        editForm.add(parent);

        final TextFieldBootstrapFormComponent<Integer> expression = new TextFieldBootstrapFormComponent<>("expression");
        expression.setEnabled(false);
        editForm.add(expression);

        final TextFieldBootstrapFormComponent<Integer> position = new TextFieldBootstrapFormComponent<>("position");
        position.getField().setRequired(true);
        editForm.add(position);

        ColorPickerBootstrapFormComponent colorPicker = new ColorPickerBootstrapFormComponent("color",
                new PropertyModel<>(editForm.getModelObject(), "categoryStyle.color"));
        editForm.add(colorPicker);

        editForm.add(new ListViewTextTranslationSectionPanel("labels"));
        editForm.add(new UniqueLanguageTranslationValidator());
    }

}
