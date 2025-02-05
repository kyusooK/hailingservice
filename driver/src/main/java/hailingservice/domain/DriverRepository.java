package hailingservice.domain;

import hailingservice.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "drivers", path = "drivers")
public interface DriverRepository
    extends PagingAndSortingRepository<Driver, Long> {}
