package hailingservice.domain;

import hailingservice.domain.*;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "drivers", path = "drivers")
public interface DriverRepository
    extends PagingAndSortingRepository<Driver, Long> {
    @Query(
        value = "select driver " +
        "from Driver driver " +
        "where(:driverLocation is null or driver.driverLocation like %:driverLocation%)"
    )
    List<Driver> getDriverLocation(String driverLocation, Pageable pageable);
}
