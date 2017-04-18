package rokomari.PublisherInventory.service.user.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.general.Category;

import java.util.List;

public interface CategoryService {

    void createCategory(Category category);
    Category getCategory(Publisher currentPublisher, int id);
    List<Category> getAllCategory(Publisher publisher);

    Page<Category> getAllCategories(Publisher currentPublisher, Pageable pageable);

    Page<Category> getAllCategoryWithSearchKey(Publisher currentPublisher, String searchKey, Pageable pageable);

    List<Category> getAllCategoryWithKey(Publisher currentPublisher, String searchKey);

    void delete(int id);

    Category getCategoryByName(Publisher currentPublisher, String name);
}
