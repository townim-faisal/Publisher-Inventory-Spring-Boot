package rokomari.PublisherInventory.repository.user;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import rokomari.PublisherInventory.model.user.purchase.BinderOrderBook;
import rokomari.PublisherInventory.model.user.purchase.BinderOrder;

import java.util.List;


public interface BinderOrderBookRepository extends PagingAndSortingRepository<BinderOrderBook, Integer>, CrudRepository<BinderOrderBook, Integer>,JpaSpecificationExecutor {

    void deleteById(int id);

    List<BinderOrderBook> findByBinderOrder(BinderOrder binderOrder);

    BinderOrderBook findOneByBinderOrderAndId(BinderOrder binderOrder, int id);

    BinderOrderBook findOneById(int id);
}
