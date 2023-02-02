package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections4.comparators.NullComparator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devgateway.toolkit.forms.service.admin.BaseServiceEntityService;
import org.devgateway.toolkit.forms.wicket.components.table.filter.ServiceEntityFilterState;
import org.devgateway.toolkit.persistence.dto.ServiceEntity;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Data provider used to provide information about the services
 *
 * @author Viorel Chihai
 */
public class SortableServiceEntityProvider<T extends ServiceEntity> extends SortableDataProvider<T, String>
        implements IFilterStateLocator<ServiceEntityFilterState<T>> {
    private static final long serialVersionUID = 1858130875067823547L;

    protected final String serviceName;

    private final BaseServiceEntityService<T> serviceEntityService;

    private ServiceEntityFilterState<T> filterState;

    public SortableServiceEntityProvider(final BaseServiceEntityService<T> serviceEntityService, final String serviceName) {
        this.serviceEntityService = serviceEntityService;
        this.serviceName = serviceName;
    }

    @Override
    public Iterator<? extends T> iterator(final long first, final long count) {
        List<T> entities = serviceEntityService.findAll(serviceName, filterState.spec());

        if (first > entities.size()) {
            return Collections.emptyIterator();
        }

        if (getSort() != null) {
            BeanComparator comparator = new BeanComparator(getSort().getProperty(), new NullComparator(false));
            if (getSort().isAscending()) {
                Collections.sort(entities, comparator);
            } else {
                Collections.sort(entities, comparator.reversed());
            }
        }

        return entities.subList((int) first, (int) Math.min(first + count, entities.size())).iterator();
    }

    @Override
    public long size() {
        return serviceEntityService.findAll(serviceName, filterState.spec()).size();
    }

    @Override
    public IModel<T> model(final T object) {
        return Model.of(object);
    }

    @Override
    public ServiceEntityFilterState<T> getFilterState() {
        return filterState;
    }

    @Override
    public void setFilterState(final ServiceEntityFilterState<T> filterState) {
        this.filterState = filterState;
    }
}