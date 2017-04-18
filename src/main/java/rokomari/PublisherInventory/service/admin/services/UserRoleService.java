package rokomari.PublisherInventory.service.admin.services;


import rokomari.PublisherInventory.model.admin.UserRole;

import java.util.List;


public interface UserRoleService {

    void create(UserRole userRole);

    List<UserRole> getAllRoles();
}
