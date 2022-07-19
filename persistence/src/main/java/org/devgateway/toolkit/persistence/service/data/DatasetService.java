package org.devgateway.toolkit.persistence.service.data;

import org.devgateway.toolkit.persistence.dao.data.Dataset;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.UniquePropertyService;

import java.util.List;

public interface DatasetService<T extends Dataset> extends BaseJpaService<T>, UniquePropertyService<T> {

    List<T> findAllDeleted();

    List<T> findAllPublishing();

    long countByNonPublished(Integer year);

}
