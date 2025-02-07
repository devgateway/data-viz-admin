package org.devgateway.toolkit.persistence.repository.data;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.repository.norepository.UniquePropertyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import java.util.List;

@Transactional
public interface TetsimDatasetRepository extends DatasetRepository<TetsimDataset>,
        UniquePropertyRepository<TetsimDataset, Long> {

    @Query("select td from TetsimDataset td where td.status like 'DELETED'")
    List<TetsimDataset> findAllDeleted();

    @Query("select td from TetsimDataset td where td.status like 'PUBLISHING' or td.status like 'UNPUBLISHING'")
    List<TetsimDataset> findAllInProgress();

    @Query ("select count(e) from #{#entityName} e where (:year is null or e.year=:year) "
            + "and e.status not in ('DELETED')")
    long countByNonPublished(@Param("year") Integer year);

    @Query("select td from CSVDataset td where td.status not in ('DELETED') and td.destinationService like :service")
    List<TetsimDataset> findAllNotDeletedForService(@Param("service") String service);
}
