package rokomari.PublisherInventory.repository.user;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import rokomari.PublisherInventory.model.user.general.Book;
import rokomari.PublisherInventory.model.user.inventory.InventoryDetails;


public interface InventoryDetailsRepository extends PagingAndSortingRepository<InventoryDetails, Integer>, CrudRepository<InventoryDetails, Integer>,JpaSpecificationExecutor {
/*    InventoryDetails findByPublisherAndPhone(Publisher publisher, String phone);

    InventoryDetails findByPublisherAndEmail(Publisher publisher, String email);*/

    //Page<InventoryDetails> findByPublisherAndIdOrPublisherAndPhoneContainingOrPublisherAndEmailContainingOrPublisherAndNameContainingAllIgnoreCase(Publisher publisher1, int id, Publisher publisher2, String name, Publisher publisher3, String email, Publisher publisher4, String phone, Pageable pageable);

    //Page<InventoryDetails> findByPublisherAndPhoneContainingOrPublisherAndEmailContainingOrPublisherAndNameContainingAllIgnoreCase(Publisher publisher2, String name, Publisher publisher3, String email, Publisher publisher4, String phone, Pageable pageable);

    //List<InventoryDetails> findByPublisher(Publisher publisher);

    //Page<InventoryDetails> findAllByPublisher(Publisher publisher, Pageable pageable);

    InventoryDetails findOneByBook(Book book);
}
