package rokomari.PublisherInventory.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import rokomari.PublisherInventory.model.common.Area;

import java.util.List;

public interface AreaRepository extends PagingAndSortingRepository<Area, Integer>,CrudRepository<Area, Integer> {

    Page<Area> findByNameContainingIgnoreCase(String searchKey, Pageable pageable);

    List<Area> findByNameContainingIgnoreCase(String searchKey);
}
