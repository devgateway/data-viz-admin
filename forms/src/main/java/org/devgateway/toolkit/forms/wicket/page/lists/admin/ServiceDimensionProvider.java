package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devgateway.toolkit.forms.service.DatasetClientService;
import org.devgateway.toolkit.forms.service.admin.BaseServiceEntityService;
import org.devgateway.toolkit.persistence.dto.ServiceDimension;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Data provider used to provide information about the services
 *
 * @author Viorel Chihai
 */
public class ServiceDimensionProvider extends SortableServiceEntityProvider<ServiceDimension> {
    private static final long serialVersionUID = 3067952110482080616L;

    public ServiceDimensionProvider(final BaseServiceEntityService<ServiceDimension> serviceEntityService,
                                    final String serviceName) {
        super(serviceEntityService, serviceName);
    }
}