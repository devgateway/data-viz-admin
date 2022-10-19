package org.devgateway.toolkit.forms.wicket.page.edit.admin;

import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.service.admin.ServiceCategoryService;
import org.devgateway.toolkit.forms.wicket.components.form.ColorPickerBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceCategoriesPage;
import org.devgateway.toolkit.persistence.dto.ServiceCategory;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/editServiceCategory")
public class EditServiceCategoryPage extends AbstractEditServiceEntityPage<ServiceCategory> {
    private static final long serialVersionUID = -9013029067860834250L;

    @SpringBean
    private ServiceCategoryService serviceCategoryService;

    public EditServiceCategoryPage(final PageParameters parameters) {
        super(parameters);
        this.listPageClass = ListServiceCategoriesPage.class;
        this.entityService = serviceCategoryService;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final TextFieldBootstrapFormComponent<String> type = new TextFieldBootstrapFormComponent<>("type");
        type.setEnabled(false);
        editForm.add(type);

        final TextFieldBootstrapFormComponent<String> code = new TextFieldBootstrapFormComponent<>("code");
        code.setEnabled(false);
        editForm.add(code);

        final TextFieldBootstrapFormComponent<String> value = new TextFieldBootstrapFormComponent<>("value");
        value.getField().setRequired(true);
        editForm.add(value);

        final TextFieldBootstrapFormComponent<Integer> position = new TextFieldBootstrapFormComponent<>("position");
        position.getField().setRequired(true);
        editForm.add(position);

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
    }

}