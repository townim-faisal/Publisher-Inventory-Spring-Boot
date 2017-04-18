package rokomari.PublisherInventory.service.admin.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rokomari.PublisherInventory.model.admin.EndUser;

import java.util.List;

public interface EndUserService {

    void createEndUser(EndUser endUser);

    EndUser getUserByUserName(String userName);

    EndUser getEndUser(int id);

    Page<EndUser> getAllEndUsers(Pageable pageable);

    Page<EndUser> getAllEndUserWithSearchKey(String searchKey, Pageable pageable);

    List<EndUser> getAllEndUser();

    List<EndUser> getAllEndUserWithKey(String searchKey);

    void delete(int id);
}
