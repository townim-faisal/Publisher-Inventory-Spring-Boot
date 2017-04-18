package rokomari.PublisherInventory.serviceImpl.user;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.inventory.Inventory;
import rokomari.PublisherInventory.repository.user.InventoryRepository;
import rokomari.PublisherInventory.service.user.services.InventoryService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component(value = "InventoryService")
@Transactional
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void createInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    @Override
    public Inventory getInventory(Publisher publisher, int id) {
        return inventoryRepository.findOneByPublisherAndId(publisher,id);
    }

    @Override
    public List<Inventory> getAllInventory(Publisher publisher) {
        return inventoryRepository.findByPublisher(publisher);
    }

    @Override
    public Page<Inventory> getAllInventories(Publisher publisher, Pageable pageable) {
        return inventoryRepository.findAllByPublisher(publisher , pageable);
    }

    @Override
    public Page<Inventory> getAllInventoryWithSearchKey(Publisher publisher, String searchKey, Pageable pageable) {
        return inventoryRepository.findByPublisherAndNameContainingAllIgnoreCase(publisher, searchKey, pageable);
    }

    @Override
    public List<Inventory> getAllInventoryWithKey(Publisher publisher, String searchKey) {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(Inventory.class).get();

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
                        .must(queryBuilder.keyword().onFields("id","name").matching(searchKey).createQuery())
                        .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, Inventory.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List<Inventory> results = jpaQuery.getResultList();

        return results;
    }

    @Override
    public void delete(int id) {
        inventoryRepository.delete(id);
    }

    @Override
    public Inventory getInventoryByName(Publisher publisher, String name) {
        return inventoryRepository.findByPublisherAndName(publisher , name);
    }
}
