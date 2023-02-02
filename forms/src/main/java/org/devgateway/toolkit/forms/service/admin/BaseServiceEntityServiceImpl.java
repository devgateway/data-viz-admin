package org.devgateway.toolkit.forms.service.admin;

import org.devgateway.toolkit.forms.client.ServiceEntityClient;
import org.devgateway.toolkit.forms.service.EurekaClientService;
import org.devgateway.toolkit.persistence.dto.ServiceEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class BaseServiceEntityServiceImpl<T extends ServiceEntity> implements BaseServiceEntityService<T> {

    @Override
    public List<T> findAll(final String serviceName) {
        return serviceEntityClient(serviceName).findAll();
    }

    @Override
    public List<T> findAll(final String serviceName, final Predicate<T> spec) {
        return findAll(serviceName).stream()
                .filter(spec)
                .collect(Collectors.toList());
    }

    @Override
    public T findOne(final String serviceName, final Long id) {
        return serviceEntityClient(serviceName).findOne(id);
    }

    @Override
    public void save(final String serviceName, final T entity) {
        serviceEntityClient(serviceName).save(entity);
    }

    public void update(final String serviceName, final T entity) {
        serviceEntityClient(serviceName).update(entity);
    }

    public void delete(final String serviceName, final T entity) {
        serviceEntityClient(serviceName).delete(entity);
    }

    @Override
    public long count(String serviceName) {
        return 0;
    }

    @Override
    public T newInstance() {
        return null;
    }

    protected abstract ServiceEntityClient<T> serviceEntityClient(final String serviceName);
}
