package rokomari.PublisherInventory.service.user.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.Binder;

import java.util.List;

public interface BinderService {

    void createBinder(Binder binder);
    Binder getBinder(Publisher currentPublisher, int id);
    List<Binder> getAllBinders(Publisher publisher);

    Binder getBinderByPhone(Publisher publisher, String phone);
    Binder getBinderByEmail(Publisher currentPublisher, String email);

    void delete(int id);

    Page<Binder> getAllBinders(Publisher publisher, Pageable pageable);

    List<Binder> getAllBinderWithKey(Publisher publisher, String binder);

    Page<Binder> getAllBinderWithSearchKey(Publisher publisher, String searchKey, Pageable pageable);
}
