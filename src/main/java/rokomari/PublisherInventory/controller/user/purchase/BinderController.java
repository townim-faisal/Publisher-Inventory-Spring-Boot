package rokomari.PublisherInventory.controller.user.purchase;

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
import rokomari.PublisherInventory.service.admin.services.AreaService;
import rokomari.PublisherInventory.service.admin.services.CityService;
import rokomari.PublisherInventory.service.admin.services.CountryService;
import rokomari.PublisherInventory.service.common.PublisherAndUser;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.Binder;
import rokomari.PublisherInventory.service.user.services.BinderService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/binder")
public class BinderController {


    @Autowired
    BinderService binderService;

    @Autowired
    CityService cityService;

    @Autowired
    AreaService areaService;

    @Autowired
    CountryService countryService;

    @Autowired
    PublisherAndUser publisherAndUser;

    private Publisher getCurrentPublisher(){

        return publisherAndUser.getCurrentPublisher();
    }

    @RequestMapping(value="/new-binder", method = RequestMethod.GET)
    public String newBinder(Model model){

        publisherAndUser.publisherAndUser(model);

        Binder binder = new Binder();
        binder.setPublisher(getCurrentPublisher());
        model.addAttribute("page_title", "Add Binder");
        model.addAttribute("addNewBinder", binder);
        model.addAttribute("allCity", cityService.getAllCity());
        model.addAttribute("allArea",areaService.getAllArea());
        model.addAttribute("allCountry",countryService.getAllCountry());

        return "user/binder/new";
    }

    @RequestMapping(value="/new-binder", method = RequestMethod.POST)
    public String newBinder(@Valid @ModelAttribute("addNewBinder") Binder binder, BindingResult bindingResult,
                            Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model);

        int currentId = binder.getId(); // Storing the old value
        String currentName = binder.getName();

        model.addAttribute("addNewBinder", binder);
        model.addAttribute("page_title", "Add Binder");

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");
            /*System.out.println("Error : "+bindingResult.getAllErrors());*/
            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added");

                return "redirect:/binder/new-binder";
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");

                return "redirect:/binder/edit/" + currentId;
            }
        }

        try{

            if(isPhoneExist(binder.getPhone(),currentId).equals("true") || isEmailExist(binder.getEmail(), currentId).equals("true")){
                binderService.createBinder(binder); // this operation changes the id number after creating new entry
                clearTextBoxes(binder);
            }
            else{
                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added or updated");

                return "redirect:/binder/new-binder";
            }

            binder.setId(currentId); // setting the old value
            currentId=binder.getId();

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
                return "redirect:/binder/new-binder";

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");
                return "redirect:/binder/edit/" + currentId;
            }
        }

        if(currentId == 0){

            return "redirect:/binder/new-binder";
        }

        else{

            return "redirect:/binder/all-binder";
        }
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editBinder(@PathVariable int id, Model model ){

        /*model.addAttribute("publisher_name_navbar", getPublisherName());*/
        publisherAndUser.publisherAndUser(model);

        Binder binder = binderService.getBinder(getCurrentPublisher(), id);

        model.addAttribute("page_title", "Update Author");
        model.addAttribute("city",binder.getAddress().getCity());
        model.addAttribute("area",binder.getAddress().getArea());
        model.addAttribute("country",binder.getAddress().getCountry());
        model.addAttribute("allCity", cityService.getAllCity());
        model.addAttribute("allArea",areaService.getAllArea());
        model.addAttribute("allCountry",countryService.getAllCountry());
        model.addAttribute("addNewBinder", binder);

        return "user/binder/new";
    }


    @RequestMapping(value = "/all-binder", method = RequestMethod.GET)
    public String allBinder(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        publisherAndUser.publisherAndUser(model);

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<Binder> binders;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "name");


        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            binders = binderService.getAllBinders(getCurrentPublisher(),pageable);
        }
        else {

            binders = binderService.getAllBinderWithSearchKey(getCurrentPublisher(),searchKey, pageable);
        }

        int totalElements = (int) binders.getTotalElements();
        if (totalElements % elementPerPage == 0) {
            numberOfPage = totalElements / elementPerPage;
        } else if (totalElements % elementPerPage > 0) {
            numberOfPage = (totalElements / elementPerPage) + 1;
        }

        model.addAttribute("page_title", "All Binders");
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("binderList", binders);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("binderNames",sortedBinders(getCurrentPublisher()));
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "user/binder/all";
    }

    private List<Binder> sortedBinders(Publisher publisher){

        List<Binder> bindersSortedList = binderService.getAllBinders(publisher);
        Collections.sort(bindersSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        return bindersSortedList;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchBinder(@RequestParam("term") String searchKey){

        List<Binder> bindersSortedList = binderService.getAllBinderWithKey(getCurrentPublisher(), searchKey);
        Collections.sort(bindersSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        List<String> names = new ArrayList<>();

        for (Binder binder: bindersSortedList){
            names.add(binder.getName());
        }

        return names;
    }

    @RequestMapping(value = "/binder-detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Binder binderDetail(@PathVariable int id){

        return binderService.getBinder(getCurrentPublisher(),id);
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteBinder(@PathVariable int id, RedirectAttributes redirectAttributes){

        binderService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","Binder Deleted");

        return "redirect:/binder/all-binder";
    }

    @RequestMapping("/isEmailExist")
    @ResponseBody
    public String isEmailExist(@RequestParam String email, @RequestParam int id){

        try{
            if(binderService.getBinderByEmail(getCurrentPublisher(), email).getId() == id || binderService.getBinderByEmail(getCurrentPublisher(), email) == null){

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


    @RequestMapping("/isPhoneExist")
    @ResponseBody
    public String isPhoneExist(@RequestParam String phone, @RequestParam int id){

        try{
            if(binderService.getBinderByPhone(getCurrentPublisher(), phone).getId() == id || binderService.getBinderByPhone(getCurrentPublisher(), phone) == null){

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

    private void clearTextBoxes(Binder binder){

        binder.setPhone(null);
        binder.setName(null);
        binder.setAddress(null);
        binder.setEmail(null);
    }








}
