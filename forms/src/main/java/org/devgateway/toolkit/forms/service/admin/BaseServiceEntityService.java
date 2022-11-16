package org.devgateway.toolkit.forms.service.admin;

import org.devgateway.toolkit.persistence.dto.ServiceEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public interface BaseServiceEntityService<T extends ServiceEntity> {

    List<T> findAll(String serviceName);

    List<T> findAll(String serviceName, Predicate<T> spec);

    T findOne(String serviceName, Long id);

    void save(String serviceName, T entity);

    void update(String serviceName, T entity);

    long count(String serviceName);

    T newInstance();

}
