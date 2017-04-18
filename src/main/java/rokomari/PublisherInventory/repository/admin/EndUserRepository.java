package rokomari.PublisherInventory.repository.admin;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import rokomari.PublisherInventory.model.admin.EndUser;

import java.util.List;

public interface EndUserRepository extends PagingAndSortingRepository<EndUser, Integer>, CrudRepository<EndUser, Integer> {
    EndUser findOneByName(String userName);
    List<EndUser> findByNameContainingIgnoreCase(String userName);

    Page<EndUser> findByNameContainingIgnoreCase(String searchKey, Pageable pageable);
}
