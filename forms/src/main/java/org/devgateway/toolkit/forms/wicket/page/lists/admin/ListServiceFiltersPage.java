package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.service.admin.ServiceFilterService;
import org.devgateway.toolkit.forms.wicket.page.edit.admin.EditServiceFilterPage;
import org.devgateway.toolkit.persistence.dto.ServiceFilter;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/filters")
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
        columns.add(new PropertyColumn<>(new Model<>("Value"), "code", "code"));
        columns.add(new PropertyColumn<>(new Model<>("System Label"), "value", "value"));
        columns.add(new PropertyColumn<>(new Model<>("Type"), "fieldType", "fieldType"));
    }

}