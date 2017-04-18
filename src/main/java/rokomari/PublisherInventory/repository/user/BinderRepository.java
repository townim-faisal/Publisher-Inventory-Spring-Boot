package rokomari.PublisherInventory.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.Binder;

import java.util.List;


public interface BinderRepository extends PagingAndSortingRepository<Binder, Integer>, CrudRepository<Binder, Integer> {
    Binder findByPublisherAndPhone(Publisher publisher, String phone);

    Binder findByPublisherAndEmail(Publisher publisher, String email);

    Page<Binder> findByPublisherAndPhoneContainingOrPublisherAndEmailContainingOrPublisherAndNameContainingAllIgnoreCase(Publisher publisher , String name, Publisher publisher1, String email, Publisher publisher3, String phone, Pageable pageable);

    Page<Binder> findByPublisherAndIdOrPublisherAndPhoneContainingOrPublisherAndEmailContainingOrPublisherAndNameContainingAllIgnoreCase(Publisher publisher , int id,Publisher publisher0 , String name, Publisher publisher1, String email, Publisher publisher3, String phone, Pageable pageable);

    List<Binder> findByPublisher(Publisher publisher);

    Page<Binder> findAllByPublisher(Publisher publisher, Pageable pageable);

    Binder findOneByPublisherAndId(Publisher publisher, int id);
}
