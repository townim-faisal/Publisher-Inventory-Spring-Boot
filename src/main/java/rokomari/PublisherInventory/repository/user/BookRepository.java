package rokomari.PublisherInventory.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.general.Book;

import java.util.List;


public interface BookRepository extends CrudRepository<Book, Integer> {
    
    Book findOneByPublisherAndId(Publisher publisher, int id);

    Page<Book> findAllByPublisher(Publisher publisher, Pageable pageable);

    Page<Book> findByPublisherAndIdOrPublisherAndIsbnContainingOrPublisherAndNameContainingAllIgnoreCase(Publisher publisher, int id, Publisher publisher1, String searchKey, Publisher publisher2, String searchKey1, Pageable pageable);

    Page<Book> findByPublisherAndIsbnContainingOrPublisherAndNameContainingAllIgnoreCase(Publisher publisher, String searchKey, Publisher publisher1, String searchKey1, Pageable pageable);

    List<Book> findByPublisher(Publisher publisher);

    Book findByPublisherAndIsbn(Publisher publisher, String isbn);
}
