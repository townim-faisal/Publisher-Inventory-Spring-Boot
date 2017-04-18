package rokomari.PublisherInventory.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.BinderReceive;

import java.util.Calendar;
import java.util.List;


public interface BinderReceiveRepository extends PagingAndSortingRepository<BinderReceive, Integer>, CrudRepository<BinderReceive, Integer>,JpaSpecificationExecutor {
    List<BinderReceive> findByPublisherAndOrderReceiveDate(Publisher publisher, Calendar currentDate);

    BinderReceive findOneByPublisherAndId(Publisher publisher, int id);

    Page<BinderReceive> findAllByPublisher(Publisher publisher, Pageable pageable);

    List<BinderReceive> findByPublisher(Publisher publisher);

    BinderReceive findByPublisherAndReceiveId(Publisher publisher, String orderId);

    Page<BinderReceive> findByPublisherAndIdOrPublisherAndReceiveIdContainingAllIgnoreCase(Publisher publisher, int id, Publisher publisher1, String searchKey, Pageable pageable);

    Page<BinderReceive> findByPublisherAndReceiveIdContainingAllIgnoreCase(Publisher publisher, String searchKey, Pageable pageable);
}
