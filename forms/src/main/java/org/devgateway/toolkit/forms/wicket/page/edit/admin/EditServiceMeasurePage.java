package org.devgateway.toolkit.forms.wicket.page.edit.admin;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceDimensionsPage;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceMeasuresPage;
import org.devgateway.toolkit.persistence.dto.ServiceMeasure;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu Page used to make editing easy, extend to get easy access
 * to one entity for editing
 */
@MountPath(value = "/editServiceMeasure")
public class EditServiceMeasurePage extends AbstractEditServiceEntityPage<ServiceMeasure> {
    private static final long serialVersionUID = -9013029067860834250L;

    public EditServiceMeasurePage(final PageParameters parameters) {
        super(parameters);
        this.listPageClass = ListServiceMeasuresPage.class;
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
    }

    @Override
    protected ServiceMeasure newInstance() {
        return new ServiceMeasure();
    }

    @Override
    protected ServiceMeasure getEntityById(final Long id) {
        return datasetClientService.getMeasureById(serviceName, id);
    }

    @Override
    protected void createEntity(final ServiceMeasure entity) {
        datasetClientService.addMeasureToService(serviceName, entity);
    }

    @Override
    protected void updateEntity(final ServiceMeasure entity) {
        datasetClientService.updateMeasure(serviceName, entity);
    }

}
