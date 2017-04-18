package rokomari.PublisherInventory.service.user.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.BinderReceive;

import java.util.Calendar;
import java.util.List;

public interface BinderReceiveService {
    void createBinderReceive(BinderReceive binderReceive);

    List<BinderReceive> getAllOrdersByDate(Publisher currentPublisher, Calendar currentDate);

    BinderReceive getBinderReceive(Publisher currentPublisher, int id);

    Page<BinderReceive> getAllBinderReceives(Publisher currentPublisher, Pageable pageable);

    Page<BinderReceive> getAllBinderReceivesWithSearchKey(Publisher currentPublisher, String searchKey, Pageable pageable);

    List<BinderReceive> getAllBinderReceives(Publisher publisher);

    List<BinderReceive> getAllBinderReceivesWithKey(Publisher currentPublisher, String searchKey);

    void delete(int id);

    BinderReceive getBinderReceiveOrderByReceiveId(Publisher currentPublisher, String receiveId);

}
