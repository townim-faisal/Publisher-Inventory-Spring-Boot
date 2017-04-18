package rokomari.PublisherInventory.serviceImpl.admin;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.admin.EndUser;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.repository.admin.EndUserRepository;
import rokomari.PublisherInventory.service.admin.services.EndUserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Component(value = "EndUserService")
@Transactional
public class EndUserServiceImpl implements EndUserService {

    @Autowired
    EndUserRepository endUserRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createEndUser(EndUser endUser) {
        endUserRepository.save(endUser);
    }

    @Override
    public EndUser getUserByUserName(String userName) {
        return endUserRepository.findOneByName(userName);
    }

    @Override
    public EndUser getEndUser(int id) {
        return endUserRepository.findOne(id);
    }

    @Override
    public Page<EndUser> getAllEndUsers(Pageable pageable) {
        return endUserRepository.findAll(pageable);
    }

    @Override
    public Page<EndUser> getAllEndUserWithSearchKey(String searchKey, Pageable pageable) {
        return endUserRepository.findByNameContainingIgnoreCase(searchKey, pageable);
    }

    @Override
    public List<EndUser> getAllEndUser() {
        return (List<EndUser>) endUserRepository.findAll();
    }

    @Override
    public List<EndUser> getAllEndUserWithKey(String searchKey) {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(EndUser.class).get();

        // a very basic query by keywords
        org.apache.lucene.search.Query query =
                queryBuilder
                        .keyword()
                        .onFields("id","name","publisher.name")
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
                fullTextEntityManager.createFullTextQuery(query, EndUser.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List<EndUser> results = jpaQuery.getResultList();

        return results;
    }

    @Override
    public void delete(int id) {
        endUserRepository.delete(id);
    }
}
