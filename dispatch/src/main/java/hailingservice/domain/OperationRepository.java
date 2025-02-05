package hailingservice.domain;

import hailingservice.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "operations",
    path = "operations"
)
public interface OperationRepository
    extends PagingAndSortingRepository<Operation, Long> {}
