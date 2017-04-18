package rokomari.PublisherInventory.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import rokomari.PublisherInventory.service.common.PublisherAndUser;

@Controller
public class HomeController {

    @Autowired
    PublisherAndUser publisherAndUser;

    @RequestMapping(value={"/","/home"})
    public String home(Model model) {

        publisherAndUser.publisherAndUser(model);

        model.addAttribute("page_title", "Home");

        return "home";
    }
}
