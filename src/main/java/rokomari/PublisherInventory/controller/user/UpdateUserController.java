package rokomari.PublisherInventory.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rokomari.PublisherInventory.service.common.PublisherAndUser;
import rokomari.PublisherInventory.model.admin.EndUser;
import rokomari.PublisherInventory.service.admin.services.EndUserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/update")
public class UpdateUserController {

    @Autowired
    EndUserService endUserService;

    @Autowired
    PublisherAndUser publisherAndUser;

    private int getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return endUserService.getUserByUserName(authentication.getName()).getId();
    }

    @RequestMapping(value="/edit-user", method = RequestMethod.GET)
    public String newEndUser(Model model){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Update Account Information");

        EndUser endUser = endUserService.getEndUser(getCurrentUser());

        model.addAttribute("updateEndUser", endUser);

        return "user/update/update";
    }


    @RequestMapping(value="/update-user", method = RequestMethod.POST)
    public String newEndUser(@Valid @ModelAttribute("updateEndUser") EndUser user, BindingResult bindingResult,
                               Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Update Account Information");

        int currentId = getCurrentUser();
        String password = user.getPassword();

        user = endUserService.getEndUser(currentId);
        String currentName = user.getName();
        user.setPassword(password);

       // model.addAttribute("updateEndUser", user);

        try{
            endUserService.createEndUser(user);
            clearTextBoxes(user);

            redirectAttributes.addFlashAttribute("status", "successful");
            redirectAttributes.addFlashAttribute("message",currentName+" updated Successfully");
        }
        catch (Exception e){

            redirectAttributes.addFlashAttribute("status", "unsuccessful");
            redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");
            return "redirect:/update/edit-user";
        }

        return "redirect:/update/edit-user";

    }

    private void clearTextBoxes(EndUser user){

        //user.setPhone(null);
        user.setName(null);
        //user.setAddress(null);
    }
}
