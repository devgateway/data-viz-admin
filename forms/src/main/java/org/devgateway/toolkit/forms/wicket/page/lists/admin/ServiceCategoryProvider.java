package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import org.devgateway.toolkit.forms.service.admin.BaseServiceEntityService;
import org.devgateway.toolkit.persistence.dto.ServiceCategory;

import java.util.Comparator;

/**
 * Data provider used to provide information about the services
 *
 * @author Viorel Chihai
 */
public class ServiceCategoryProvider extends SortableServiceEntityProvider<ServiceCategory> {
    private static final long serialVersionUID = 3067952110482080616L;

    public ServiceCategoryProvider(final BaseServiceEntityService<ServiceCategory> serviceEntityService,
                                   final String serviceName) {
        super(serviceEntityService, serviceName);
    }

}