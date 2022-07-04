package org.devgateway.toolkit.forms.wicket.page.edit.admin;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.admin.ListServicePage;
import org.devgateway.toolkit.persistence.dao.ServiceMetadata;
import org.devgateway.toolkit.persistence.service.ServiceMetadataService;

public class EditServicePage extends AbstractEditPage<ServiceMetadata> {

    @SpringBean
    protected ServiceMetadataService serviceMetadataService;

    public EditServicePage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = serviceMetadataService;
        this.listPageClass = ListServicePage.class;

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        editForm.add(new TextFieldBootstrapFormComponent<>("name").required());
        editForm.add(new TextFieldBootstrapFormComponent<>("description"));
        editForm.add(new TextFieldBootstrapFormComponent<>("url"));
    }

}
