package rokomari.PublisherInventory.service.admin.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rokomari.PublisherInventory.model.admin.Publisher;

import java.util.List;

public interface PublisherService {
    void createPublisher(Publisher publisher);

    Publisher getPublisher(int id);

    Page<Publisher> getAllPublishers(Pageable pageable);

    Page<Publisher> getAllPublisherWithSearchKey(String searchKey, Pageable pageable);

    List<Publisher> getAllPublisher();

    List<Publisher> getAllPublisherWithKey(String searchKey);

    void delete(int id);

    Publisher getPublisherByPhone(String phone);

    Publisher getPublisherByEmail(String email);
}
