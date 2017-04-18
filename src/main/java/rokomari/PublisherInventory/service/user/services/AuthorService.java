package rokomari.PublisherInventory.service.user.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.general.Author;

import java.util.List;

public interface AuthorService {

    void createAuthor(Author author);
    Author getAuthor(Publisher currentPublisher, int id);
    List<Author> getAllAuthors(Publisher publisher);

    Author getAuthorByPhone(Publisher publisher, String phone);
    Author getAuthorByEmail(Publisher currentPublisher, String email);

    void delete(int id);

    Page<Author> getAllAuthors(Publisher publisher, Pageable pageable);

    List<Author> getAllAuthorWithKey(Publisher publisher, String author);

    Page<Author> getAllAuthorWithSearchKey(Publisher publisher, String searchKey, Pageable pageable);

}
