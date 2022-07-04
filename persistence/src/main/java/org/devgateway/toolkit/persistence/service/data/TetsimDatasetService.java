package org.devgateway.toolkit.persistence.service.data;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.UniquePropertyService;

import java.util.List;

public interface TetsimDatasetService extends BaseJpaService<TetsimDataset>, UniquePropertyService<TetsimDataset> {

    List<TetsimDataset> findAllDeleted();

    List<TetsimDataset> findAllPublishing();

    long countByNonPublished(Integer year);
}
