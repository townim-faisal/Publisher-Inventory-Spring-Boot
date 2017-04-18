package rokomari.PublisherInventory.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.Binder;
import rokomari.PublisherInventory.model.user.purchase.BinderOrder;

import java.util.Calendar;
import java.util.List;


public interface BinderOrderRepository extends PagingAndSortingRepository<BinderOrder, Integer>, CrudRepository<BinderOrder, Integer>,JpaSpecificationExecutor {
    List<BinderOrder> findByPublisherAndOrderPlaceDate(Publisher publisher, Calendar currentDate);

    BinderOrder findOneByPublisherAndId(Publisher publisher, int id);

    Page<BinderOrder> findAllByPublisher(Publisher publisher, Pageable pageable);

    List<BinderOrder> findByPublisher(Publisher publisher);

    BinderOrder findByPublisherAndOrderId(Publisher publisher, String orderId);

    Page<BinderOrder> findByPublisherAndIdOrPublisherAndOrderIdContainingAllIgnoreCase(Publisher publisher, int id, Publisher publisher1, String searchKey, Pageable pageable);

    Page<BinderOrder> findByPublisherAndOrderIdContainingAllIgnoreCase(Publisher publisher, String searchKey, Pageable pageable);

    BinderOrder findByPublisherAndIdAndOrderPlaceDate(Publisher publisher, int id, Calendar currentDate);

    List<BinderOrder> findByPublisherAndBinderAndConfirmationStatus(Publisher publisher, Binder binder, BinderOrder.ConfirmationStatus confirmed);
}
