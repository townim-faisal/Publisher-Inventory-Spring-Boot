package rokomari.PublisherInventory.serviceImpl.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.BinderOrder;
import rokomari.PublisherInventory.model.user.purchase.BinderOrderBook;
import rokomari.PublisherInventory.repository.user.BinderOrderBookRepository;
import rokomari.PublisherInventory.service.user.services.BinderOrderBookService;

import java.util.List;

@Component(value = "BinderOrderDetailsService")
@Transactional
public class BinderOrderBookServiceImpl implements BinderOrderBookService {

    @Autowired
    BinderOrderBookRepository binderOrderBookRepository;


    @Override
    public void delete(int id) {
        binderOrderBookRepository.deleteById(id);
    }

    @Override
    public List<BinderOrderBook> getBinderOrderBookList(Publisher publisher, BinderOrder binderOrder) {
        return binderOrderBookRepository.findByBinderOrder(binderOrder);
    }
    @Override
    public void createBinderOrderBook(BinderOrderBook binderOrderBook){
        binderOrderBookRepository.save(binderOrderBook);
    }

    @Override
    public BinderOrderBook getBinderOrderBook(BinderOrder binderOrder, int id) {
        return binderOrderBookRepository.findOneByBinderOrderAndId(binderOrder, id);
    }

    @Override
    public BinderOrderBook getBinderOrderBook(Publisher publisher, int id) {
        return binderOrderBookRepository.findOneById(id);
    }

}
