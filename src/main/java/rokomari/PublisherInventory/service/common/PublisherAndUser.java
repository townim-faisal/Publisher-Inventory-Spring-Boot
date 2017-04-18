package rokomari.PublisherInventory.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import rokomari.PublisherInventory.model.admin.UserRole;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.service.admin.services.EndUserService;

import java.util.List;

@Component(value = "publisherAndUser")
public class PublisherAndUser {

    @Autowired
    EndUserService endUserService;

    private String getPublisherName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return endUserService.getUserByUserName(authentication.getName()).getPublisher().getName();
    }

    private boolean getAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.isAuthenticated();
    }

    private List<UserRole> getRoles(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return endUserService.getUserByUserName(authentication.getName()).getRoles();
    }

    public void publisherAndUser(Model model) {

        model.addAttribute("authenticated", getAuthentication());
        model.addAttribute("publisher_name_navbar", getPublisherName());

        for (int i= 0;i<getRoles().size();i++ ) {

            if (getRoles().get(i).getRole().contains("USER") ||
                    getRoles().get(i).getRole().contains("ADMIN")) {
                model.addAttribute("user", "client");
            } else if (getRoles().get(i).getRole().contains("SUPER")){
                model.addAttribute("user", "superAdmin");
            }
        }
    }

    public Publisher getCurrentPublisher(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return endUserService.getUserByUserName(authentication.getName()).getPublisher();
    }
}
