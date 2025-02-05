package hailingservice.domain;

import hailingservice.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "matchings", path = "matchings")
public interface MatchingRepository
    extends PagingAndSortingRepository<Matching, Long> {}
