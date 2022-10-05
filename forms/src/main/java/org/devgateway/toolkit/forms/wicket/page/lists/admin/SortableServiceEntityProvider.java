package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devgateway.toolkit.forms.service.DatasetClientService;
import org.devgateway.toolkit.forms.service.admin.BaseServiceEntityService;
import org.devgateway.toolkit.persistence.dto.ServiceDimension;
import org.devgateway.toolkit.persistence.dto.ServiceEntity;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Data provider used to provide information about the services
 *
 * @author Viorel Chihai
 */
public class SortableServiceEntityProvider<T extends ServiceEntity> extends SortableDataProvider<T, String> {
    private static final long serialVersionUID = 1858130875067823547L;

    protected final String serviceName;

    private final BaseServiceEntityService<T> serviceEntityService;

    public SortableServiceEntityProvider(final BaseServiceEntityService<T> serviceEntityService, final String serviceName) {
        this.serviceEntityService = serviceEntityService;
        this.serviceName = serviceName;
    }

    @Override
    public Iterator<? extends T> iterator(final long first, final long count) {
        List<T> entities = serviceEntityService.findAll(serviceName);

        if (first > entities.size()) {
            return Collections.emptyIterator();
        }

        if (getSort() == null || getSort().getProperty().equals("label")) {
            Comparator<T> comparator = getServiceEntityComparator();

            if (getSort() != null && getSort().isAscending()) {
                comparator = comparator.reversed();
            }

            Collections.sort(entities, comparator);
        }

        return entities.subList((int) first, (int) Math.min(first + count, entities.size())).iterator();
    }

    @Override
    public long size() {
        return serviceEntityService.findAll(serviceName).size();
    }

    @Override
    public IModel<T> model(final T object) {
        return Model.of(object);
    }

    protected Comparator<T> getServiceEntityComparator() {
        return Comparator.comparingLong(ServiceEntity::getId);
    }
}