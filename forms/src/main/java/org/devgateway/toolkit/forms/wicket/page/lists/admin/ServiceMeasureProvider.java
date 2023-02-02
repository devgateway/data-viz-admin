package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import org.devgateway.toolkit.forms.service.admin.BaseServiceEntityService;
import org.devgateway.toolkit.persistence.dto.ServiceMeasure;

/**
 * Data provider used to provide information about the services
 *
 * @author Viorel Chihai
 */
public class ServiceMeasureProvider extends SortableServiceEntityProvider<ServiceMeasure> {
    private static final long serialVersionUID = 1858130875067823547L;

    public ServiceMeasureProvider(BaseServiceEntityService<ServiceMeasure> serviceEntityService, final String serviceName) {
        super(serviceEntityService, serviceName);
    }

}