package rokomari.PublisherInventory.serviceImpl.user;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.general.Category;
import rokomari.PublisherInventory.repository.user.CategoryRepository;
import rokomari.PublisherInventory.service.user.services.CategoryService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component(value = "CategoryService")
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Category getCategory(Publisher publisher, int id) {
        return categoryRepository.findOneByPublisherAndId(publisher,id);
    }

    @Override
    public List<Category> getAllCategory(Publisher publisher) {
        return categoryRepository.findByPublisher(publisher);
    }

    @Override
    public Page<Category> getAllCategories(Publisher publisher, Pageable pageable) {
        return categoryRepository.findAllByPublisher(publisher , pageable);
    }

    @Override
    public Page<Category> getAllCategoryWithSearchKey(Publisher publisher, String searchKey, Pageable pageable) {
        return categoryRepository.findByPublisherAndNameContainingAllIgnoreCase(publisher, searchKey, pageable);
    }

    @Override
    public List<Category> getAllCategoryWithKey(Publisher publisher, String searchKey) {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(Category.class).get();

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
                fullTextEntityManager.createFullTextQuery(query, Category.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List<Category> results = jpaQuery.getResultList();

        return results;
    }

    @Override
    public void delete(int id) {
        categoryRepository.delete(id);
    }

    @Override
    public Category getCategoryByName(Publisher publisher, String name) {
        return categoryRepository.findByPublisherAndName(publisher , name);
    }
}
