package rokomari.PublisherInventory.service.user.services;


import rokomari.PublisherInventory.model.user.general.Book;
import rokomari.PublisherInventory.model.user.inventory.InventoryDetails;

public interface InventoryDetailsService {

    void createInventoryDetails(InventoryDetails inventoryDetails);
    InventoryDetails getInventoryDetails(Book book);
    //List<InventoryDetails> getAllInventoryDetailss(Publisher publisher);

    /*InventoryDetails getInventoryDetailsByPhone(Publisher publisher, String phone);
    InventoryDetails getInventoryDetailsByEmail(Publisher currentPublisher, String email);*/

    void delete(int id);

    //Page<InventoryDetails> getAllInventoryDetailss(Publisher publisher, Pageable pageable);

    //List<InventoryDetails> getAllInventoryDetailsWithKey(Publisher publisher, String author);

    //Page<InventoryDetails> getAllInventoryDetailsWithSearchKey(Publisher publisher, String searchKey, Pageable pageable);

}
