package rokomari.PublisherInventory.serviceImpl.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.admin.UserRole;
import rokomari.PublisherInventory.repository.admin.UserRoleRepository;
import rokomari.PublisherInventory.service.admin.services.UserRoleService;

import java.util.List;

@Component(value = "EndUserRoleService")
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public void create(UserRole userRole) {

        userRoleRepository.save(userRole);
    }

    @Override
    public List<UserRole> getAllRoles() {
        return (List<UserRole>) userRoleRepository.findAll();
    }
}
