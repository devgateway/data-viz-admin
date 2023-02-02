package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import org.devgateway.toolkit.forms.service.admin.BaseServiceEntityService;
import org.devgateway.toolkit.persistence.dto.ServiceDataset;

/**
 * Data provider used to provide information about the services
 *
 * @author Viorel Chihai
 */
public class ServiceDatasetProvider extends SortableServiceEntityProvider<ServiceDataset> {
    private static final long serialVersionUID = 5486671831226922079L;

    public ServiceDatasetProvider(final BaseServiceEntityService<ServiceDataset> serviceEntityService,
                                  final String serviceName) {
        super(serviceEntityService, serviceName);
    }
}