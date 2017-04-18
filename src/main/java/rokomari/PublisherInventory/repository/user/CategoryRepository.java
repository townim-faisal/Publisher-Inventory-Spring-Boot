package rokomari.PublisherInventory.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.general.Category;

import java.util.List;


public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Category findByNameContainingIgnoreCase(String name);

    Category findOneByPublisherAndId(Publisher publisher, int id);

    List<Category> findByPublisher(Publisher publisher);

    Page<Category> findAllByPublisher(Publisher publisher, Pageable pageable);

    Page<Category> findByPublisherAndNameContainingAllIgnoreCase(Publisher publisher, String searchKey, Pageable pageable);

    Category findByPublisherAndName(Publisher publisher, String name);
}
