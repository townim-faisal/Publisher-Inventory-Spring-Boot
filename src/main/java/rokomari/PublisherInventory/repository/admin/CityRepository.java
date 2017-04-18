package rokomari.PublisherInventory.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import rokomari.PublisherInventory.model.common.City;

import java.util.List;

public interface CityRepository extends PagingAndSortingRepository<City, Integer>,CrudRepository<City, Integer> {

    Page<City> findByNameContainingIgnoreCase(String searchKey, Pageable pageable);

    List<City> findByNameContainingIgnoreCase(String searchKey);
}
