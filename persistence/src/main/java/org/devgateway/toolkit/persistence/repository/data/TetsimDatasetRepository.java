package org.devgateway.toolkit.persistence.repository.data;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;

import javax.transaction.Transactional;

@Transactional
public interface TetsimDatasetRepository extends DatasetRepository<TetsimDataset> {

}
