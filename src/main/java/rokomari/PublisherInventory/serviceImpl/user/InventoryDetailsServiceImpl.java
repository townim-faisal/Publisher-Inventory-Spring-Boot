package rokomari.PublisherInventory.serviceImpl.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.user.general.Book;
import rokomari.PublisherInventory.model.user.inventory.InventoryDetails;
import rokomari.PublisherInventory.repository.user.InventoryDetailsRepository;
import rokomari.PublisherInventory.service.user.services.InventoryDetailsService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component(value = "InventoryDetailsService")
@Transactional
public class InventoryDetailsServiceImpl implements InventoryDetailsService {


    @Autowired
    private InventoryDetailsRepository inventoryDetailsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public InventoryDetails getInventoryDetails(Book book){
        return inventoryDetailsRepository.findOneByBook(book);
    }

    /*@Override
    public List<InventoryDetails> getAllInventoryDetailss(Publisher publisher){
        return inventoryDetailsRepository.findByPublisher(publisher);
    }*/

    /*@Override
    public InventoryDetails getInventoryDetailsByPhone(Publisher publisher, String phone) {
        return inventoryDetailsRepository.findByPublisherAndPhone(publisher , phone);
    }*/

    /*@Override
    public InventoryDetails getInventoryDetailsByEmail(Publisher publisher, String email) {
        return inventoryDetailsRepository.findByPublisherAndEmail(publisher , email);
    }*/

    @Override
    public void delete(int id) {
        inventoryDetailsRepository.delete(id);
    }
/*
    @Override
    public Page<InventoryDetails> getAllInventoryDetailss(Publisher publisher, Pageable pageable) {
        return inventoryDetailsRepository.findAllByPublisher(publisher , pageable);
    }*/

    /*@Override
    public List<InventoryDetails> getAllInventoryDetailsWithKey(Publisher publisher, String searchKey){

        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(InventoryDetails.class).get();

        // a very basic query by keywords
       *//* org.apache.lucene.search.Query query =
                queryBuilder
                        .keyword()
                        .onFields("name", "phone", "email")
                        .matching(author)
                        .createQuery();*//*

        org.apache.lucene.search.Query query =
                queryBuilder
                        .bool()
                        .must(queryBuilder.keyword().onField("publisher.id").matching(String.valueOf(publisher.getId())).createQuery())
                        .must(queryBuilder.keyword().onFields("id").matching(searchKey).createQuery())
                        .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, InventoryDetails.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List<InventoryDetails> results = jpaQuery.getResultList();

        return results;
    }*/

//    @Override
//    public Page<InventoryDetails> getAllInventoryDetailsWithSearchKey(Publisher publisher, String searchKey, Pageable pageable) {
//
//        int id;
//        try {
//            id = Integer.parseInt(searchKey);
//
//            //return inventoryDetailsRepository.findByPublisherAndIdOrPublisherAndPhoneContainingOrPublisherAndEmailContainingOrPublisherAndNameContainingAllIgnoreCase(publisher, id, publisher, searchKey,publisher,searchKey,publisher,searchKey, pageable);
//        }
//        catch (NumberFormatException e){
//            //return inventoryDetailsRepository.findByPublisherAndPhoneContainingOrPublisherAndEmailContainingOrPublisherAndNameContainingAllIgnoreCase(publisher, searchKey,publisher,searchKey,publisher,searchKey, pageable);
//        }
//    }

    @Override
    public void createInventoryDetails(InventoryDetails author){
        inventoryDetailsRepository.save(author);
    }


}
