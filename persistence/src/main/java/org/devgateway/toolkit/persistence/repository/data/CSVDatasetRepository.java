package org.devgateway.toolkit.persistence.repository.data;

import org.devgateway.toolkit.persistence.dao.data.CSVDataset;
import org.devgateway.toolkit.persistence.repository.norepository.UniquePropertyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import java.util.List;

@Transactional
public interface CSVDatasetRepository extends DatasetRepository<CSVDataset>,
        UniquePropertyRepository<CSVDataset, Long> {

    @Query("select td from CSVDataset td where td.status like 'DELETED'")
    List<CSVDataset> findAllDeleted();

    @Query("select td from CSVDataset td where td.status like 'PUBLISHING' or td.status like 'UNPUBLISHING'")
    List<CSVDataset> findAllInProgress();

    @Query ("select count(e) from #{#entityName} e where (:year is null or e.year=:year) "
            + "and e.status not in ('DELETED')")
    long countByNonPublished(@Param("year") Integer year);

    @Query("select td from CSVDataset td where td.status not like 'DELETED' and td.destinationService like :service")
    List<CSVDataset> findAllNotDeletedForService(@Param("service") String service);
}
