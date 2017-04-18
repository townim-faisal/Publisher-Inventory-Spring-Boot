package rokomari.PublisherInventory.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.general.Author;

import java.util.List;


public interface AuthorRepository extends PagingAndSortingRepository<Author, Integer>, CrudRepository<Author, Integer>,JpaSpecificationExecutor {
    Author findByPublisherAndPhone(Publisher publisher, String phone);

    Author findByPublisherAndEmail(Publisher publisher, String email);

    Page<Author> findByPublisherAndIdOrPublisherAndPhoneContainingOrPublisherAndEmailContainingOrPublisherAndNameContainingAllIgnoreCase(Publisher publisher1 ,int id, Publisher publisher2 ,String name,Publisher publisher3,String email,Publisher publisher4, String phone, Pageable pageable);

    Page<Author> findByPublisherAndPhoneContainingOrPublisherAndEmailContainingOrPublisherAndNameContainingAllIgnoreCase(Publisher publisher2 ,String name,Publisher publisher3,String email,Publisher publisher4, String phone, Pageable pageable);

    List<Author> findByPublisher(Publisher publisher);

    Page<Author> findAllByPublisher(Publisher publisher, Pageable pageable);

    Author findOneByPublisherAndId(Publisher publisher, int id);
}
