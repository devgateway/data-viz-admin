package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devgateway.toolkit.forms.service.DatasetClientService;
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
public class ServiceDimensionProvider extends SortableDataProvider<ServiceDimension, String> {
    private static final long serialVersionUID = 1858130875067823547L;

    protected DatasetClientService datasetClientService;

    private final String serviceName;

    public ServiceDimensionProvider(final DatasetClientService datasetClientService, final String serviceName) {
        this.datasetClientService = datasetClientService;
        this.serviceName = serviceName;
    }

    @Override
    public Iterator<ServiceDimension> iterator(final long first, final long count) {
        List<ServiceDimension> services = datasetClientService.getDimensions(serviceName);

        if (first > services.size()) {
            return Collections.emptyIterator();
        }

        if (getSort() == null || getSort().getProperty().equals("label")) {
            Comparator comparator = new ServiceDimensionComparator();

            if (getSort() != null && getSort().isAscending()) {
                comparator = comparator.reversed();
            }

            Collections.sort(services, comparator);
        }

        return services.subList((int) first, (int) Math.min(first + count, services.size())).iterator();
    }

    @Override
    public SortParam<String> getSort() {
        return super.getSort();
    }

    @Override
    public long size() {
        return datasetClientService.getDimensions(serviceName).size();
    }

    @Override
    public IModel<ServiceDimension> model(final ServiceDimension serviceDimension) {
        return Model.of(serviceDimension);
    }

    protected class ServiceDimensionComparator implements Comparator<ServiceDimension> {
        @Override
        public int compare(final ServiceDimension s1, final ServiceDimension s2) {
            return String.CASE_INSENSITIVE_ORDER.compare(s1.getCode(), s2.getCode());
        }
    }

}