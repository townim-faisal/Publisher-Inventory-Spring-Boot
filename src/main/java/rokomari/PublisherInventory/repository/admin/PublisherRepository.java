package rokomari.PublisherInventory.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import rokomari.PublisherInventory.model.admin.Publisher;

import java.util.List;


public interface PublisherRepository extends PagingAndSortingRepository<Publisher, Integer>, CrudRepository<Publisher, Integer> {
    Publisher findByPhone(String phone);

    List<Publisher> findByNameContainingIgnoreCase(String publisher);

    Page<Publisher> findByNameContainingIgnoreCase(String searchKey, Pageable pageable);

    Publisher findByEmail(String email);

    Page<Publisher> findByIdOrNameContainingOrPhoneContainingOrEmailContainingIgnoreCase(int id, String searchKey, String searchKey1, String searchKey2, Pageable pageable);

    Page<Publisher> findByNameContainingOrPhoneContainingOrEmailContainingIgnoreCase(String searchKey, String searchKey1, String searchKey2, Pageable pageable);

    Publisher findOneById(int i);
}
