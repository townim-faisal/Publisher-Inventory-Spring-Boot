package rokomari.PublisherInventory.controller.common;

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
import rokomari.PublisherInventory.model.common.Country;
import rokomari.PublisherInventory.service.admin.services.CountryService;
import rokomari.PublisherInventory.service.common.PublisherAndUser;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/country")
public class CountryController {


    @Autowired
    CountryService countryService;

    @Autowired
    PublisherAndUser publisherAndUser;

    @RequestMapping(value="/new-country", method = RequestMethod.GET)
    public String newUser(Model model){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Add Country");

        model.addAttribute("addNewCountry",new Country());

        return "admin/country/new";
    }

    @RequestMapping(value="/new-country", method = RequestMethod.POST)
    public String newCountry(@Valid @ModelAttribute("addNewCountry") Country country, BindingResult bindingResult,
                               Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Add Country");

        int currentId = country.getId(); // Storing the old value
        String currentName = country.getName();

        model.addAttribute("addNewCountry", country);

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");

            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added");

                return "redirect:/country/new-country";
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");

                return "redirect:/country/edit/" + currentId;
            }
        }

        try{
            countryService.createCountry(country); // this operation changes the id number after creating new entry
            clearTextBoxes(country);

            country.setId(currentId); // setting the old value
            currentId = country.getId();

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
                return "redirect:/country/new-country";

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");
                return "redirect:/country/edit/" + currentId;
            }
        }

        if(currentId == 0){

            return "redirect:/country/new-country"; //redirects to method, not html page
        }

        else{

            return "redirect:/country/all-country";
        }
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editCountry(@PathVariable int id, Model model ){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Update Country");

        model.addAttribute("addNewCountry", countryService.getCountry(id));

        return "admin/country/new";
    }

    @RequestMapping(value = "/all-country", method = RequestMethod.GET)
    public String allCountry(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        publisherAndUser.publisherAndUser(model);

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<Country> countryList;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "name");


        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            countryList = countryService.getAllCountry(pageable);
        }
        else {

            countryList = countryService.getAllCountryWithSearchKey(searchKey, pageable);
        }

        int totalElements = (int) countryList.getTotalElements();

        if (totalElements % elementPerPage == 0) {
            numberOfPage = totalElements / elementPerPage;
        } else if (totalElements % elementPerPage > 0) {
            numberOfPage = (totalElements / elementPerPage) + 1;
        }
        model.addAttribute("page_title", "All Countries");
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("countryList", countryList);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("countryNames", sortedCountry());
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "admin/country/all";
    }

    private List<Country> sortedCountry(){

        List<Country> countrySortedList = countryService.getAllCountry();
        Collections.sort(countrySortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        return countrySortedList;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchCountry(@RequestParam("term") String searchKey){

        List<Country> countrySortedList = countryService.getAllCountryWithKey(searchKey);
        Collections.sort(countrySortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        List<String> names = new ArrayList<>();

        for (Country country: countrySortedList){
            names.add(country.getName());
        }

        return names;
    }

    @RequestMapping(value = "/country-detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Country countryDetail(@PathVariable int id){

        return countryService.getCountry(id);
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteCountry(@PathVariable int id, RedirectAttributes redirectAttributes){

        countryService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","country Deleted");

        return "redirect:/country/all-country";
    }

    private void clearTextBoxes(Country country){

        country.setName(null);
    }
}
