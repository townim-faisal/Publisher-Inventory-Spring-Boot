package rokomari.PublisherInventory.service.user.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.general.Book;

import java.util.List;

public interface BookService {

    void createBook(Book book);
    Book getBook(Publisher currentPublisher, int id);

    Page<Book> getAllBooks(Publisher currentPublisher, Pageable pageable);

    Page<Book> getAllBookWithSearchKey(Publisher currentPublisher, String searchKey, Pageable pageable);

    List<Book> getAllBooks(Publisher publisher);

    List<Book> getAllBookWithKey(Publisher currentPublisher, String searchKey);

    void delete(int id);

    Book getBookByIsbn(Publisher currentPublisher, String isbn);
}
