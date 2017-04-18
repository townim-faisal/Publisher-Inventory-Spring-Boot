package rokomari.PublisherInventory.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rokomari.PublisherInventory.model.admin.EndUser;
import rokomari.PublisherInventory.model.admin.UserRole;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.service.admin.services.UserRoleService;
import rokomari.PublisherInventory.service.admin.services.EndUserService;
import rokomari.PublisherInventory.service.admin.services.PublisherService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/supersecret")
public class SuperSecretController {

    @Autowired
    EndUserService endUserService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    PublisherService publisherService;

    private List<UserRole> role(){

        UserRole userRoleUser = new UserRole();
        userRoleUser.setId(10);
        userRoleUser.setRole("USER");

        UserRole userRoleAdmin = new UserRole();
        userRoleUser.setId(20);
        userRoleAdmin.setRole("ADMIN");

        UserRole userRoleSuperAdmin = new UserRole();
        userRoleUser.setId(30);
        userRoleSuperAdmin.setRole("SUPER");

        if (userRoleService.getAllRoles().size()<3) {

            userRoleService.create(userRoleUser);
            userRoleService.create(userRoleAdmin);
            userRoleService.create(userRoleSuperAdmin);
        }

        List<UserRole> roles = new ArrayList<>();

        roles.add(userRoleSuperAdmin);

        return roles;
    }

    private Publisher publisher(){

        Publisher publisher = new Publisher();

        publisher.setName("Rokomari");
        publisher.setPhone("0000");
        publisherService.createPublisher(publisher);
        return publisher;
    }


//    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value="/supersecret-user", method = RequestMethod.GET)
    @ResponseBody
    public String newEndUser(Model model){

        EndUser user = new EndUser();

        user.setName("supersecret");
        user.setRoles(role());
        user.setPassword("supersecret");
        user.setPublisher(publisher());
        user.setIsEnabled(true);

        endUserService.createEndUser(user);


        return "All are supersecret.\n. This operation may remove user role data base and recreate it";
    }


}
