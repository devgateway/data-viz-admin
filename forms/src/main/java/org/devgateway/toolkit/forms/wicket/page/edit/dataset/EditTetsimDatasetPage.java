package org.devgateway.toolkit.forms.wicket.page.edit.dataset;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.dataset.ListTetsimDatasetPage;
import org.devgateway.toolkit.persistence.dao.data.DatasetStatus;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author vchihai
 */
@MountPath(value = "/editTetsimDataset")
public class EditTetsimDatasetPage extends AbstractEditPage<TetsimDataset> {

    private static final long serialVersionUID = -8460878260874111506L;

    @SpringBean
    protected TetsimDatasetService tetsimDatasetService;

    public EditTetsimDatasetPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tetsimDatasetService;
        this.listPageClass = ListTetsimDatasetPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final TextFieldBootstrapFormComponent<Integer> year = new TextFieldBootstrapFormComponent<>("year");
        year.required();
        year.integer();
        editForm.add(year);

        final StringValue id = getPageParameters().get(WebConstants.PARAM_ID);

        if (id.isEmpty()) {
            editForm.getModelObject().setStatus(DatasetStatus.DRAFT);
        }
    }
}
