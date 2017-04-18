package rokomari.PublisherInventory.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import rokomari.PublisherInventory.model.common.Country;

import java.util.List;

public interface CountryRepository extends PagingAndSortingRepository<Country, Integer>,CrudRepository<Country, Integer> {

    Page<Country> findByNameContainingIgnoreCase(String searchKey, Pageable pageable);

    List<Country> findByNameContainingIgnoreCase(String searchKey);
}
