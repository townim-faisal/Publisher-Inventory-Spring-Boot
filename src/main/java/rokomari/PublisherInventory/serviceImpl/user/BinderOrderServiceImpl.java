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
import rokomari.PublisherInventory.model.user.purchase.BinderOrder;
import rokomari.PublisherInventory.repository.user.BinderOrderRepository;
import rokomari.PublisherInventory.service.user.services.BinderOrderService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.List;

@Component(value = "BinderOrderService")
@Transactional
public class BinderOrderServiceImpl implements BinderOrderService {

    @Autowired
    private BinderOrderRepository binderOrderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createBinderOrder(BinderOrder binderOrder) {
        binderOrderRepository.save(binderOrder);
    }

    @Override
    public List<BinderOrder> getAllOrdersByDate(Publisher publisher, Calendar currentDate) {
        return binderOrderRepository.findByPublisherAndOrderPlaceDate(publisher , currentDate);
    }

    @Override
    public BinderOrder getBinderOrder(Publisher publisher, int id) {
        return binderOrderRepository.findOneByPublisherAndId(publisher,id);
    }

    @Override
    public Page<BinderOrder> getAllBinderOrders(Publisher publisher, Pageable pageable) {
        return binderOrderRepository.findAllByPublisher(publisher , pageable);
    }

    @Override
    public Page<BinderOrder> getAllBinderOrderWithSearchKey(Publisher publisher, String searchKey, Pageable pageable) {
        int id;
        try {
            id = Integer.parseInt(searchKey);

            return binderOrderRepository.findByPublisherAndIdOrPublisherAndOrderIdContainingAllIgnoreCase(publisher, id, publisher, searchKey, pageable);
        }
        catch (NumberFormatException e){
            return binderOrderRepository.findByPublisherAndOrderIdContainingAllIgnoreCase(publisher, searchKey, pageable);
        }
    }

    @Override
    public List<BinderOrder> getAllBinderOrders(Publisher publisher) {
        return binderOrderRepository.findByPublisher(publisher);
    }

    @Override
    public List<BinderOrder> getAllBinderOrderWithKey(Publisher publisher, String searchKey) {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(BinderOrder.class).get();

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
                        .must(queryBuilder.keyword().onFields("id","orderId","binder.id","binder.name").matching(searchKey).createQuery())
                        .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, BinderOrder.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List<BinderOrder> results = jpaQuery.getResultList();

        return results;
    }

    @Override
    public void delete(int id) {
        binderOrderRepository.delete(id);
    }

    @Override
    public BinderOrder getBinderOrderByOrderId(Publisher publisher, String orderId) {
        return binderOrderRepository.findByPublisherAndOrderId(publisher,orderId);
    }

    @Override
    public BinderOrder getBinderOrderByOrderPlaceDate(Publisher publisher, int id, Calendar currentDate) {
        return binderOrderRepository.findByPublisherAndIdAndOrderPlaceDate(publisher,id,currentDate);
    }

    @Override
    public List<BinderOrder> getAllConfirmedOrder(Publisher publisher, Binder binder, BinderOrder.ConfirmationStatus confirmed) {
        return binderOrderRepository.findByPublisherAndBinderAndConfirmationStatus(publisher,binder, BinderOrder.ConfirmationStatus.CONFIRMED);
    }
}
