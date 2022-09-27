package org.devgateway.toolkit.forms.wicket.page.lists.admin;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.persistence.dto.ServiceMetadata;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Data provider used to provide information about the services
 *
 * @author Viorel Chihai
 */
public class ServiceMetadataProvider extends SortableDataProvider<ServiceMetadata, String> {
    private static final long serialVersionUID = 1858130875067823547L;

    protected EurekaClientService eurekaClientService;

    public ServiceMetadataProvider(final EurekaClientService eurekaClientService) {
        this.eurekaClientService = eurekaClientService;
    }

    @Override
    public Iterator<ServiceMetadata> iterator(final long first, final long count) {
        List<ServiceMetadata> services = eurekaClientService.findAllWithData();

        if (first > services.size()) {
            return Collections.emptyIterator();
        }

        if (getSort() == null || getSort().getProperty().equals("label")) {
            Comparator comparator = new ServiceMetadataComparator();

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
        return eurekaClientService.findAllWithData().size();
    }

    @Override
    public IModel<ServiceMetadata> model(final ServiceMetadata serviceMetadata) {
        return Model.of(serviceMetadata);
    }

    protected class ServiceMetadataComparator implements Comparator<ServiceMetadata> {
        @Override
        public int compare(final ServiceMetadata s1, final ServiceMetadata s2) {
            return String.CASE_INSENSITIVE_ORDER.compare(s1.getName(), s2.getName());
        }
    }

}