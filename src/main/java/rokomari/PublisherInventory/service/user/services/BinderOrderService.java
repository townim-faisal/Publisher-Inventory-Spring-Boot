package rokomari.PublisherInventory.service.user.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.Binder;
import rokomari.PublisherInventory.model.user.purchase.BinderOrder;

import java.util.Calendar;
import java.util.List;

public interface BinderOrderService {
    void createBinderOrder(BinderOrder binderOrder);

    List<BinderOrder> getAllOrdersByDate(Publisher currentPublisher, Calendar currentDate);

    BinderOrder getBinderOrder(Publisher currentPublisher, int id);

    Page<BinderOrder> getAllBinderOrders(Publisher currentPublisher, Pageable pageable);

    Page<BinderOrder> getAllBinderOrderWithSearchKey(Publisher currentPublisher, String searchKey, Pageable pageable);

    List<BinderOrder> getAllBinderOrders(Publisher publisher);

    List<BinderOrder> getAllBinderOrderWithKey(Publisher currentPublisher, String searchKey);

    void delete(int id);

    BinderOrder getBinderOrderByOrderId(Publisher currentPublisher, String orderId);

    BinderOrder getBinderOrderByOrderPlaceDate(Publisher currentPublisher, int id, Calendar currentDate);

    List<BinderOrder> getAllConfirmedOrder(Publisher currentPublisher, Binder id, BinderOrder.ConfirmationStatus confirmed);
}
