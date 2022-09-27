package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devgateway.toolkit.forms.service.DatasetClientService;
import org.devgateway.toolkit.persistence.dto.ServiceDimension;
import org.devgateway.toolkit.persistence.dto.ServiceMeasure;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Data provider used to provide information about the services
 *
 * @author Viorel Chihai
 */
public class ServiceMeasureProvider extends SortableDataProvider<ServiceMeasure, String> {
    private static final long serialVersionUID = 1858130875067823547L;

    protected DatasetClientService datasetClientService;

    private final String serviceName;

    public ServiceMeasureProvider(final DatasetClientService datasetClientService, final String serviceName) {
        this.datasetClientService = datasetClientService;
        this.serviceName = serviceName;
    }

    @Override
    public Iterator<ServiceMeasure> iterator(final long first, final long count) {
        List<ServiceMeasure> services = datasetClientService.getMeasures(serviceName);

        if (first > services.size()) {
            return Collections.emptyIterator();
        }

        if (getSort() == null || getSort().getProperty().equals("label")) {
            Comparator comparator = new ServiceMeasureComparator();

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
        return datasetClientService.getMeasures(serviceName).size();
    }

    @Override
    public IModel<ServiceMeasure> model(final ServiceMeasure serviceMeasure) {
        return Model.of(serviceMeasure);
    }

    protected class ServiceMeasureComparator implements Comparator<ServiceMeasure> {
        @Override
        public int compare(final ServiceMeasure s1, final ServiceMeasure s2) {
            return String.CASE_INSENSITIVE_ORDER.compare(s1.getCode(), s2.getCode());
        }
    }

}