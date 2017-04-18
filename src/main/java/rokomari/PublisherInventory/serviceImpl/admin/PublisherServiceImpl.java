package rokomari.PublisherInventory.serviceImpl.admin;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.repository.admin.PublisherRepository;
import rokomari.PublisherInventory.service.admin.services.PublisherService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component(value = "PublisherService")
@Transactional
public class PublisherServiceImpl implements PublisherService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PublisherRepository publisherRepository;

    @Override
    public void createPublisher(Publisher publisher) {
        publisherRepository.save(publisher);
    }

    @Override
    public Publisher getPublisher(int id) {
        return publisherRepository.findOne(id);
    }

    @Override
    public Page<Publisher> getAllPublishers(Pageable pageable) {
        return publisherRepository.findAll(pageable);
    }

    @Override
    public Page<Publisher> getAllPublisherWithSearchKey(String searchKey, Pageable pageable) {

        int id;
        try {
            id = Integer.parseInt(searchKey);
            return publisherRepository.findByIdOrNameContainingOrPhoneContainingOrEmailContainingIgnoreCase(id, searchKey, searchKey, searchKey, pageable);
        }
        catch (NumberFormatException e){
            return publisherRepository.findByNameContainingOrPhoneContainingOrEmailContainingIgnoreCase(searchKey, searchKey, searchKey, pageable);
        }
    }

    @Override
    public List<Publisher> getAllPublisher() {
        return (List<Publisher>) publisherRepository.findAll();
    }

    @Override
    public List<Publisher> getAllPublisherWithKey(String searchKey) {

        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(Publisher.class).get();

        // a very basic query by keywords
        org.apache.lucene.search.Query query =
                queryBuilder
                        .keyword()
                        .onFields("id","name", "phone","email")
                        .matching(searchKey)
                        .createQuery();

        /*org.apache.lucene.search.Query query =
                queryBuilder
                        .bool()
                        .must(queryBuilder.keyword().onField("publisher.id").matching(String.valueOf(publisher.getId())).createQuery())
                        .must(queryBuilder.keyword().onFields("name", "phone", "email").matching(searchKey).createQuery())
                        .createQuery();*/

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, Publisher.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List<Publisher> results = jpaQuery.getResultList();

        return results;

        //return publisherRepository.findByNameContainingIgnoreCase(searchKey);
    }

    @Override
    public void delete(int id) {
        publisherRepository.delete(id);
    }

    @Override
    public Publisher getPublisherByPhone(String phone) {
        return publisherRepository.findByPhone(phone);
    }

    @Override
    public Publisher getPublisherByEmail(String email) {
        return publisherRepository.findByEmail(email);
    }
}
