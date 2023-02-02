package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import org.devgateway.toolkit.forms.service.admin.BaseServiceEntityService;
import org.devgateway.toolkit.persistence.dto.ServiceDimension;
import org.devgateway.toolkit.persistence.dto.ServiceFilter;

import java.util.Comparator;

/**
 * Data provider used to provide information about the services
 *
 * @author Viorel Chihai
 */
public class ServiceFilterProvider extends SortableServiceEntityProvider<ServiceFilter> {
    private static final long serialVersionUID = 3067952110482080616L;

    public ServiceFilterProvider(final BaseServiceEntityService<ServiceFilter> serviceEntityService,
                                 final String serviceName) {
        super(serviceEntityService, serviceName);
    }
}