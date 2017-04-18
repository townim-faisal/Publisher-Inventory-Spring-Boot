package rokomari.PublisherInventory.serviceImpl.user;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.Binder;
import rokomari.PublisherInventory.repository.user.BinderRepository;
import rokomari.PublisherInventory.service.user.services.BinderService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component(value = "BinderService")
@Transactional
public class BinderServiceImpl implements BinderService {

    @Autowired
    private BinderRepository binderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Binder getBinder(Publisher publisher, int id){
        return binderRepository.findOneByPublisherAndId(publisher,id);
    }

    @Override
    public List<Binder> getAllBinders(Publisher publisher){
        /*Binder binder = */
        return binderRepository.findByPublisher(publisher);
    }

    @Override
    public Binder getBinderByPhone(Publisher publisher, String phone) {
        return binderRepository.findByPublisherAndPhone(publisher , phone);
    }

    @Override
    public Binder getBinderByEmail(Publisher publisher, String email) {
        return binderRepository.findByPublisherAndEmail(publisher , email);
    }

    @Override
    public void delete(int id) {
        binderRepository.delete(id);
    }

    @Override
    public Page<Binder> getAllBinders(Publisher publisher, Pageable pageable) {
        return binderRepository.findAllByPublisher(publisher , pageable);
    }

    @Override
    public List<Binder> getAllBinderWithKey(Publisher publisher, String searchKey){

        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(Binder.class).get();

        // a very basic query by keywords
       /* org.apache.lucene.search.Query query =
                queryBuilder
                        .keyword()
                        .onFields("name", "phone", "email")
                        .matching(binder)
                        .createQuery();*/

        org.apache.lucene.search.Query query =
                queryBuilder
                        .bool()
                        .must(queryBuilder.keyword().onField("publisher.id").matching(String.valueOf(publisher.getId())).createQuery())
                        .must(queryBuilder.keyword().onFields("id","name", "phone", "email").matching(searchKey).createQuery())
                        .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, Binder.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List<Binder> results = jpaQuery.getResultList();

        return results;
    }

    @Override
    public Page<Binder> getAllBinderWithSearchKey(Publisher publisher, String searchKey, Pageable pageable) {

        int id;
        try {
            id = Integer.parseInt(searchKey);
            return binderRepository.findByPublisherAndIdOrPublisherAndPhoneContainingOrPublisherAndEmailContainingOrPublisherAndNameContainingAllIgnoreCase(publisher, id,publisher, searchKey,publisher,searchKey,publisher,searchKey, pageable);
        }
        catch (NumberFormatException e){
            return binderRepository.findByPublisherAndPhoneContainingOrPublisherAndEmailContainingOrPublisherAndNameContainingAllIgnoreCase(publisher, searchKey,publisher,searchKey,publisher,searchKey, pageable);
        }
    }

    @Override
    public void createBinder(Binder binder){
        binderRepository.save(binder);
    }

}
