package rokomari.PublisherInventory.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.inventory.Inventory;

import java.util.List;


public interface InventoryRepository extends CrudRepository<Inventory, Integer> {
    Inventory findByNameContainingIgnoreCase(String name);

    Inventory findOneByPublisherAndId(Publisher publisher, int id);

    List<Inventory> findByPublisher(Publisher publisher);

    Page<Inventory> findAllByPublisher(Publisher publisher, Pageable pageable);

    Page<Inventory> findByPublisherAndNameContainingAllIgnoreCase(Publisher publisher, String searchKey, Pageable pageable);

    Inventory findByPublisherAndName(Publisher publisher, String name);
}
