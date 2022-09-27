package org.devgateway.toolkit.forms.wicket.page.edit.admin;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import nl.dries.wicket.hibernate.dozer.DozerModel;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.exceptions.NullListPageClassException;
import org.devgateway.toolkit.forms.service.DatasetClientService;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapCancelButton;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapSubmitButton;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.visitors.GenericBootstrapValidationVisitor;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServiceDimensionsPage;
import org.devgateway.toolkit.persistence.dto.ServiceDimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu Page used to make editing easy, extend to get easy access
 * to one entity for editing
 */
@MountPath(value = "/editServiceDimension")
public class EditServiceDimensionPage extends AbstractEditServiceEntityPage<ServiceDimension> {
    private static final long serialVersionUID = -1594571319284288551L;

    public EditServiceDimensionPage(final PageParameters parameters) {
        super(parameters);
        this.listPageClass = ListServiceDimensionsPage.class;
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
    protected ServiceDimension newInstance() {
        return new ServiceDimension();
    }

    @Override
    protected ServiceDimension getEntityById(final Long id) {
        return datasetClientService.getDimensionById(serviceName, id);
    }

    @Override
    protected void createEntity(final ServiceDimension entity) {
        datasetClientService.addDimensionToService(serviceName, entity);
    }

    @Override
    protected void updateEntity(final ServiceDimension entity) {
        datasetClientService.updateDimension(serviceName, entity);
    }

}
