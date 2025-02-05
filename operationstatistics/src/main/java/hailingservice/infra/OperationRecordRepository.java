package hailingservice.infra;

import hailingservice.domain.*;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "operationRecords",
    path = "operationRecords"
)
public interface OperationRecordRepository
    extends PagingAndSortingRepository<OperationRecord, Long> {}
