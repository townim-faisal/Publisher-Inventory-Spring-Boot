package rokomari.PublisherInventory.service.user.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.inventory.Inventory;

import java.util.List;

public interface InventoryService {

    void createInventory(Inventory inventory);
    Inventory getInventory(Publisher currentPublisher, int id);
    List<Inventory> getAllInventory(Publisher publisher);

    Page<Inventory> getAllInventories(Publisher publisher, Pageable pageable);

    Page<Inventory> getAllInventoryWithSearchKey(Publisher currentPublisher, String searchKey, Pageable pageable);

    List<Inventory> getAllInventoryWithKey(Publisher currentPublisher, String searchKey);

    void delete(int id);

    Inventory getInventoryByName(Publisher currentPublisher, String name);
}
