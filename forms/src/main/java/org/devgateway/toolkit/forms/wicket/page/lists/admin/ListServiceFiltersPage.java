package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.filter.BootstrapTextFilteredPropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.service.admin.ServiceFilterService;
import org.devgateway.toolkit.forms.wicket.components.breadcrumbs.BreadCrumbPage;
import org.devgateway.toolkit.forms.wicket.components.table.filter.ServiceEntityFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.ServiceFilterFilterState;
import org.devgateway.toolkit.forms.wicket.page.DataServicePage;
import org.devgateway.toolkit.forms.wicket.page.edit.admin.EditServiceFilterPage;
import org.devgateway.toolkit.persistence.dto.ServiceFilter;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;

@MountPath(value = "/filters")
@BreadCrumbPage(parent = DataServicePage.class, hasServiceParam = true)
public class ListServiceFiltersPage extends AbstractListServiceEntityPage<ServiceFilter> {

    private static final long serialVersionUID = -6619848875255960857L;

    @SpringBean
    private ServiceFilterService serviceFilterService;

    public ListServiceFiltersPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.serviceEntityService = serviceFilterService;
        this.editPageClass = EditServiceFilterPage.class;

        String service = pageParameters.get(WebConstants.PARAM_SERVICE).toString();
        dataProvider = new ServiceFilterProvider(serviceFilterService, service);

        columns = new ArrayList<>();
        columns.add(new BootstrapTextFilteredPropertyColumn<>(new Model<>("Value"), "code", "code", "code"));
        columns.add(new BootstrapTextFilteredPropertyColumn<>(new Model<>("System Label"), "value", "value", "value"));
        columns.add(new BootstrapTextFilteredPropertyColumn<>(new Model<>("Type"), "fieldType", "fieldType", "fieldType"));
    }

    @Override
    public ServiceEntityFilterState<ServiceFilter> newFilterState() {
        return new ServiceFilterFilterState();
    }

}