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
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.service.admin.services.AreaService;
import rokomari.PublisherInventory.service.admin.services.CityService;
import rokomari.PublisherInventory.service.admin.services.CountryService;
import rokomari.PublisherInventory.service.admin.services.PublisherService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/publisher")
public class PublisherController {

    @Autowired
    PublisherService publisherService;

    @Autowired
    CityService cityService;

    @Autowired
    AreaService areaService;

    @Autowired
    CountryService countryService;

    @Autowired
    PublisherAndUser publisherAndUser;

    /*@PreAuthorize("hasAuthority('ADMIN')")*/
    @RequestMapping(value="/new-publisher", method = RequestMethod.GET)
    public String newPublisher(Model model){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Add Publisher");

        Publisher publisher = new Publisher();


        model.addAttribute("addNewPublisher", publisher);
        model.addAttribute("allCity", cityService.getAllCity());
        model.addAttribute("allArea",areaService.getAllArea());
        model.addAttribute("allCountry",countryService.getAllCountry());

        return "admin/publisher/new";
    }

    // This method is serving both Create and Update operation

    @RequestMapping(value="/new-publisher", method = RequestMethod.POST)
    public String newPublisher(@Valid @ModelAttribute("addNewPublisher") Publisher publisher, BindingResult bindingResult,
                                  Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Add Publisher");

        int currentId = publisher.getId(); // Storing the old value
        String currentName = publisher.getName();

        model.addAttribute("addNewPublisher", publisher);

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");

            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added");

                return "redirect:/publisher/new-publisher";
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");

                return "redirect:/publisher/edit/" + currentId;
            }
        }

        try{

            if(isPhoneExist(publisher.getPhone(),currentId).equals("true") || isEmailExist(publisher.getEmail(), currentId).equals("true")){
                publisherService.createPublisher(publisher); // this operation changes the id number after creating new entry
                clearTextBoxes(publisher);
            }
            else{
                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added or updated");

                return "redirect:/author/new-author";
            }
            /*publisherService.createPublisher(publisher); // this operation changes the id number after creating new entry
            clearTextBoxes(publisher);*/

            publisher.setId(currentId); // setting the old value
            currentId=publisher.getId();

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
                return "redirect:/publisher/new-publisher";

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");
                return "redirect:/publisher/edit/" + currentId;
            }
        }

        if(currentId == 0){

            return "redirect:/publisher/new-publisher";
        }

        else{

            return "redirect:/publisher/all-publisher";
        }
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editPublisher(@PathVariable int id, Model model ){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Update Publisher");

        Publisher publisher = publisherService.getPublisher(id);

        model.addAttribute("city",publisher.getAddress().getCity());
        model.addAttribute("area",publisher.getAddress().getArea());
        model.addAttribute("country",publisher.getAddress().getCountry());

        model.addAttribute("allCity", cityService.getAllCity());
        model.addAttribute("allArea",areaService.getAllArea());
        model.addAttribute("allCountry",countryService.getAllCountry());

        model.addAttribute("addNewPublisher", publisher);

        return "admin/publisher/new";
    }


    @RequestMapping(value = "/all-publisher", method = RequestMethod.GET)
    public String allPublisher(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        publisherAndUser.publisherAndUser(model);

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<Publisher> publisherList;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "name");


        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            publisherList = publisherService.getAllPublishers(pageable);
        }
        else {

            publisherList = publisherService.getAllPublisherWithSearchKey(searchKey, pageable);
        }

        int totalElements = (int) publisherList.getTotalElements();

        if (totalElements % elementPerPage == 0) {
            numberOfPage = totalElements / elementPerPage;
        }
        else if (totalElements % elementPerPage > 0) {
            numberOfPage = (totalElements / elementPerPage) + 1;
        }

        model.addAttribute("page_title", "All Publishers");
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("publisherList", publisherList);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("publisherNames",sortedPublishers());
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "admin/publisher/all";
    }

    private List<Publisher> sortedPublishers(){

        List<Publisher> publishersSortedList = publisherService.getAllPublisher();
        Collections.sort(publishersSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        return publishersSortedList;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchPublisher(@RequestParam("term") String searchKey){

        List<Publisher> publishersSortedList = publisherService.getAllPublisherWithKey(searchKey);
        Collections.sort(publishersSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        List<String> names = new ArrayList<>();

        for (Publisher publisher: publishersSortedList){
            names.add(publisher.getName());
        }

        return names;
    }

    @RequestMapping(value = "/publisher-detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Publisher publisherDetail(@PathVariable int id){

        return publisherService.getPublisher(id);
    }

    @RequestMapping(value = "/delete/{id}")
    public String deletePublisher(@PathVariable int id, RedirectAttributes redirectAttributes){

        publisherService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","publisher Deleted");

        return "redirect:/publisher/all-publisher";
    }

    /*@RequestMapping("/isPhoneExist")
    @ResponseBody
    public String isPhoneExist(@RequestParam String phone){

        try {

            boolean bool = publisherService.getPublisherByPhone(phone) == null;
            return String.valueOf(bool);
        }
        catch (Exception e){
            return "false";
        }
    }*/

    @RequestMapping("/isPhoneExist")
    @ResponseBody
    public String isPhoneExist(@RequestParam String phone, @RequestParam int id){

        try{
            if(publisherService.getPublisherByPhone(phone).getId() == id || publisherService.getPublisherByPhone(phone) == null){

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
    }

    @RequestMapping("/isEmailExist")
    @ResponseBody
    public String isEmailExist(@RequestParam String email, @RequestParam int id){

        try{
            if(publisherService.getPublisherByEmail(email).getId() == id || publisherService.getPublisherByEmail(email) == null){

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
    }

    private void clearTextBoxes(Publisher publisher){

        //publisher.setPhone(null);
        publisher.setName(null);

        //publisher.setAddress(null);
    }
}
