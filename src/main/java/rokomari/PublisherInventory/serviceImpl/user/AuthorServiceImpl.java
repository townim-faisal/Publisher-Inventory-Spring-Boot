package rokomari.PublisherInventory.serviceImpl.user;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.general.Author;
import rokomari.PublisherInventory.repository.user.AuthorRepository;
import rokomari.PublisherInventory.service.user.services.AuthorService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component(value = "AuthorService")
@Transactional
public class AuthorServiceImpl implements AuthorService {


    @Autowired
    private AuthorRepository authorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Author getAuthor(Publisher publisher, int id){
        return authorRepository.findOneByPublisherAndId(publisher,id);
    }

    @Override
    public List<Author> getAllAuthors(Publisher publisher){
        return authorRepository.findByPublisher(publisher);
    }

    @Override
    public Author getAuthorByPhone(Publisher publisher, String phone) {
        return authorRepository.findByPublisherAndPhone(publisher , phone);
    }

    @Override
    public Author getAuthorByEmail(Publisher publisher, String email) {
        return authorRepository.findByPublisherAndEmail(publisher , email);
    }

    @Override
    public void delete(int id) {
        authorRepository.delete(id);
    }

    @Override
    public Page<Author> getAllAuthors(Publisher publisher, Pageable pageable) {
        return authorRepository.findAllByPublisher(publisher , pageable);
    }

    @Override
    public List<Author> getAllAuthorWithKey(Publisher publisher, String searchKey){

        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(Author.class).get();

        // a very basic query by keywords
       /* org.apache.lucene.search.Query query =
                queryBuilder
                        .keyword()
                        .onFields("name", "phone", "email")
                        .matching(author)
                        .createQuery();*/

        org.apache.lucene.search.Query query =
                queryBuilder
                        .bool()
                        .must(queryBuilder.keyword().onField("publisher.id").matching(String.valueOf(publisher.getId())).createQuery())
                        .must(queryBuilder.keyword().onFields("id","name", "phone", "email").matching(searchKey).createQuery())
                        .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, Author.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List<Author> results = jpaQuery.getResultList();

        return results;
    }

    @Override
    public Page<Author> getAllAuthorWithSearchKey(Publisher publisher, String searchKey, Pageable pageable) {

        int id;
        try {
            id = Integer.parseInt(searchKey);

            return authorRepository.findByPublisherAndIdOrPublisherAndPhoneContainingOrPublisherAndEmailContainingOrPublisherAndNameContainingAllIgnoreCase(publisher, id, publisher, searchKey,publisher,searchKey,publisher,searchKey, pageable);
        }
        catch (NumberFormatException e){
            return authorRepository.findByPublisherAndPhoneContainingOrPublisherAndEmailContainingOrPublisherAndNameContainingAllIgnoreCase(publisher, searchKey,publisher,searchKey,publisher,searchKey, pageable);
        }
    }

    @Override
    public void createAuthor(Author author){
        authorRepository.save(author);
    }


}
