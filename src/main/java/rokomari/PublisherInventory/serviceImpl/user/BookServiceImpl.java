package rokomari.PublisherInventory.serviceImpl.user;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.general.Book;
import rokomari.PublisherInventory.repository.user.BookRepository;
import rokomari.PublisherInventory.service.user.services.BookService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component(value = "BookService")
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public Book getBook(Publisher publisher, int id) {
        return bookRepository.findOneByPublisherAndId(publisher,id);
    }

    @Override
    public Page<Book> getAllBooks(Publisher publisher, Pageable pageable) {
        return bookRepository.findAllByPublisher(publisher , pageable);
    }

    @Override
    public Page<Book> getAllBookWithSearchKey(Publisher publisher, String searchKey, Pageable pageable) {
        int id;
        try {
            id = Integer.parseInt(searchKey);

            return bookRepository.findByPublisherAndIdOrPublisherAndIsbnContainingOrPublisherAndNameContainingAllIgnoreCase(publisher, id, publisher, searchKey,publisher,searchKey, pageable);
        }
        catch (NumberFormatException e){
            return bookRepository.findByPublisherAndIsbnContainingOrPublisherAndNameContainingAllIgnoreCase(publisher,searchKey,publisher,searchKey, pageable);
        }
    }

    @Override
    public List<Book> getAllBooks(Publisher publisher) {
        return bookRepository.findByPublisher(publisher);
    }

    @Override
    public List<Book> getAllBookWithKey(Publisher publisher, String searchKey) {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(Book.class).get();

        // a very basic query by keywords
       /* org.apache.lucene.search.Query query =
                queryBuilder
                        .keyword()
                        .onFields("name", "phone", "email")
                        .matching(book)
                        .createQuery();*/

        org.apache.lucene.search.Query query =
                queryBuilder
                        .bool()
                        .must(queryBuilder.keyword().onField("publisher.id").matching(String.valueOf(publisher.getId())).createQuery())
                        .must(queryBuilder.keyword().onFields("id","name", "isbn").matching(searchKey).createQuery())
                        .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, Book.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List<Book> results = jpaQuery.getResultList();

        return results;
    }

    @Override
    public void delete(int id) {
        bookRepository.delete(id);
    }

    @Override
    public Book getBookByIsbn(Publisher publisher, String isbn) {
        return bookRepository.findByPublisherAndIsbn(publisher , isbn);
    }
}
