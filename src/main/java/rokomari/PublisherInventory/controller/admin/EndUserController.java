package rokomari.PublisherInventory.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rokomari.PublisherInventory.service.common.PublisherAndUser;
import rokomari.PublisherInventory.model.admin.EndUser;
import rokomari.PublisherInventory.service.admin.services.UserRoleService;
import rokomari.PublisherInventory.service.admin.services.EndUserService;
import rokomari.PublisherInventory.service.admin.services.PublisherService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/user")
public class EndUserController {

    @Autowired
    EndUserService endUserService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    PublisherService publisherService;

    @Autowired
    PublisherAndUser publisherAndUser;


//    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value="/new-user", method = RequestMethod.GET)
    public String newEndUser(Model model){

        EndUser user = new EndUser();

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Add User");

        model.addAttribute("publishers", publisherService.getAllPublisher());
        model.addAttribute("addNewEndUser", user);
        model.addAttribute("userRoles", userRoleService.getAllRoles());
        /*model.addAttribute("userStatus", status());*/

        return "admin/user/new";
    }

    // This method is serving both Create and Update operation

    @RequestMapping(value="/new-user", method = RequestMethod.POST)
    public String newEndUser(@Valid @ModelAttribute("addNewEndUser") EndUser user, BindingResult bindingResult,
                               Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Add User");

        int currentId = user.getId(); // Storing the old value
        String currentName = user.getName();
        String currentPassword = null;

        if(currentId != 0) {
             currentPassword = endUserService.getEndUser(currentId).getPassword();
        }

        System.out.println("Incoming Password: "+user.getPassword());
        System.out.println("Current Password: "+currentPassword);

        model.addAttribute("addNewEndUser", user);

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");

            System.out.println("Has error: "+bindingResult.hasErrors());

            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added");

                return "redirect:/user/new-user";
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");

                return "redirect:/user/edit/" + currentId;
            }
        }

        try{
            /*user.setCode(autoGenerateCode(user.getName()));*/
            endUserService.createEndUser(user); // this operation changes the id number after creating new entry
            clearTextBoxes(user);

            user.setId(currentId); // setting the old value
            currentId=user.getId();

            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "successful");
                redirectAttributes.addFlashAttribute("message",currentName+" added Successfully");

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "successful");
                redirectAttributes.addFlashAttribute("message",currentName+" updated Successfully");

            }
        }
        catch (Exception e){

            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added");
                return "redirect:/user/new-user";

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");
                return "redirect:/user/edit/" + currentId;
            }
        }

        if(currentId == 0){

            return "redirect:/user/new-user";
        }

        else{

            return "redirect:/user/all-user";
        }
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editEndUser(@PathVariable int id, Model model ){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Update User");

        EndUser user = endUserService.getEndUser(id);

        model.addAttribute("publishers", publisherService.getAllPublisher());
        model.addAttribute("addNewEndUser", user);
        model.addAttribute("userRoles", userRoleService.getAllRoles());
        /*model.addAttribute("userStatus", status());*/


        model.addAttribute("addNewEndUser", user);

        return "admin/user/new";
    }


    @RequestMapping(value = "/all-user", method = RequestMethod.GET)
    public String allEndUser(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "All Users");

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<EndUser> userList;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "name");


        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            userList = endUserService.getAllEndUsers(pageable);
        }
        else {

            userList = endUserService.getAllEndUserWithSearchKey(searchKey, pageable);
        }

        int totalElements = (int) userList.getTotalElements();

        if (totalElements % elementPerPage == 0) {
            numberOfPage = totalElements / elementPerPage;
        } else if (totalElements % elementPerPage > 0) {
            numberOfPage = (totalElements / elementPerPage) + 1;
        }

        model.addAttribute("totalElements", totalElements);
        model.addAttribute("userList", userList);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("userNames",sortedEndUsers());
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "admin/user/all";
    }

    private List<EndUser> sortedEndUsers(){

        List<EndUser> usersSortedList = endUserService.getAllEndUser();
        Collections.sort(usersSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        return usersSortedList;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchEndUser(@RequestParam("term") String searchKey){

        List<EndUser> usersSortedList = endUserService.getAllEndUserWithKey(searchKey);
        Collections.sort(usersSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        List<String> names = new ArrayList<>();

        for (EndUser user: usersSortedList){
            names.add(user.getName());
        }

        return names;
    }

    @RequestMapping(value = "/user-detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public EndUser userDetail(@PathVariable int id){

        return endUserService.getEndUser(id);
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteEndUser(@PathVariable int id, RedirectAttributes redirectAttributes){

        endUserService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","user Deleted");

        return "redirect:/user/all-user";
    }

    /*@RequestMapping("/isPhoneExist")
    @ResponseBody
    public String isPhoneExist(@RequestParam String phone){

        try {

            boolean bool = userService.getEndUserByPhone(phone) == null;
            return String.valueOf(bool);
        }
        catch (Exception e){
            return "false";
        }
    }*/

    /*@RequestMapping("/isIdExist")
    @ResponseBody
    public String isIdExist(@RequestParam String phone, @RequestParam int id){

        try{
            if(userService.getEndUserByPhone(phone).getId() == id || userService.getEndUserByPhone(phone) == null){

                return "true";
            }
            else {

                System.out.println("FALSE");
                return "false";
            }
        }
        catch (Exception e){

            return "true";
        }
    }*/

   /* private String autoGenerateCode(String name){

        return "Test Autogenerated Code" + name;
    }*/

    private void clearTextBoxes(EndUser user){

        //user.setPhone(null);
        user.setName(null);
        //user.setAddress(null);
    }
}
