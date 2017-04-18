package rokomari.PublisherInventory.serviceImpl.user;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.BinderReceive;
import rokomari.PublisherInventory.repository.user.BinderReceiveRepository;
import rokomari.PublisherInventory.service.user.services.BinderReceiveService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.List;

@Component(value = "BinderReceiveOrderService")
@Transactional
public class BinderReceiveServiceImpl implements BinderReceiveService {

    @Autowired
    private BinderReceiveRepository binderReceiveRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createBinderReceive(BinderReceive binderReceive) {
        binderReceiveRepository.save(binderReceive);
        System.out.println("Came here to save");
    }

    @Override
    public List<BinderReceive> getAllOrdersByDate(Publisher publisher, Calendar currentDate) {
        return binderReceiveRepository.findByPublisherAndOrderReceiveDate(publisher , currentDate);
    }

    @Override
    public BinderReceive getBinderReceive(Publisher publisher, int id) {
        return binderReceiveRepository.findOneByPublisherAndId(publisher,id);
    }

    @Override
    public Page<BinderReceive> getAllBinderReceives(Publisher publisher, Pageable pageable) {
        return binderReceiveRepository.findAllByPublisher(publisher , pageable);
    }

    @Override
    public Page<BinderReceive> getAllBinderReceivesWithSearchKey(Publisher publisher, String searchKey, Pageable pageable) {
        int id;
        try {
            id = Integer.parseInt(searchKey);

            return binderReceiveRepository.findByPublisherAndIdOrPublisherAndReceiveIdContainingAllIgnoreCase(publisher, id, publisher, searchKey, pageable);
        }
        catch (NumberFormatException e){
            return binderReceiveRepository.findByPublisherAndReceiveIdContainingAllIgnoreCase(publisher, searchKey, pageable);
        }
    }

    @Override
    public List<BinderReceive> getAllBinderReceives(Publisher publisher) {
        return binderReceiveRepository.findByPublisher(publisher);
    }

    @Override
    public List<BinderReceive> getAllBinderReceivesWithKey(Publisher publisher, String searchKey) {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(BinderReceive.class).get();

        // a very basic query by keywords
        /*org.apache.lucene.search.Query query =
                queryBuilder
                        .keyword()
                        .onFields("name", "phone", "email")
                        .matching(author)
                        .createQuery();
*/

        org.apache.lucene.search.Query query =
                queryBuilder
                        .bool()
                        .must(queryBuilder.keyword().onField("publisher.id").matching(String.valueOf(publisher.getId())).createQuery())
                        .must(queryBuilder.keyword().onFields("id","receiveId").matching(searchKey).createQuery())
                        .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, BinderReceive.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List<BinderReceive> results = jpaQuery.getResultList();

        return results;
    }

    @Override
    public void delete(int id) {
        binderReceiveRepository.delete(id);
    }

    @Override
    public BinderReceive getBinderReceiveOrderByReceiveId(Publisher publisher, String orderId) {
        return binderReceiveRepository.findByPublisherAndReceiveId(publisher,orderId);
    }

}
