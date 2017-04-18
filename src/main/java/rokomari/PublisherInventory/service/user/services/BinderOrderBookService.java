package rokomari.PublisherInventory.service.user.services;


import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.BinderOrder;
import rokomari.PublisherInventory.model.user.purchase.BinderOrderBook;

import java.util.List;

public interface BinderOrderBookService {


    void delete(int id);

    List<BinderOrderBook> getBinderOrderBookList(Publisher publisher, BinderOrder binderOrder);

    public void createBinderOrderBook(BinderOrderBook binderOrderBook);

    BinderOrderBook getBinderOrderBook(BinderOrder binderOrder, int id);

    BinderOrderBook getBinderOrderBook(Publisher currentPublisher, int id);
}
