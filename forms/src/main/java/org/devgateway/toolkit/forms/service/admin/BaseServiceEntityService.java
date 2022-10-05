package org.devgateway.toolkit.forms.service.admin;

import org.devgateway.toolkit.persistence.dto.ServiceEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaseServiceEntityService<T extends ServiceEntity> {

    List<T> findAll(String serviceName);

    T findOne(String serviceName, Long id);

    void save(String serviceName, T entity);

    void update(String serviceName, T entity);

    long count(String serviceName);

    T newInstance();

}
