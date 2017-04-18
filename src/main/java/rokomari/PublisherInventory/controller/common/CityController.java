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
import rokomari.PublisherInventory.model.common.City;
import rokomari.PublisherInventory.service.admin.services.CityService;
import rokomari.PublisherInventory.service.common.PublisherAndUser;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/city")
public class CityController {


    @Autowired
    CityService cityService;

    @Autowired
    PublisherAndUser publisherAndUser;

    @RequestMapping(value="/new-city", method = RequestMethod.GET)
    public String newUser(Model model){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Add City");

        model.addAttribute("addNewCity",new City());

        return "admin/city/new";
    }

    @RequestMapping(value="/new-city", method = RequestMethod.POST)
    public String newCity(@Valid @ModelAttribute("addNewCity") City city, BindingResult bindingResult,
                               Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Add City");

        int currentId = city.getId(); // Storing the old value
        String currentName = city.getName();

        model.addAttribute("addNewCity", city);

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");

            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added");

                return "redirect:/city/new-city";
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");

                return "redirect:/city/edit/" + currentId;
            }
        }

        try{
            cityService.createCity(city); // this operation changes the id number after creating new entry
            clearTextBoxes(city);

            city.setId(currentId); // setting the old value
            currentId = city.getId();

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
                return "redirect:/city/new-city";

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");
                return "redirect:/city/edit/" + currentId;
            }
        }

        if(currentId == 0){

            return "redirect:/city/new-city"; //redirects to method, not html page
        }

        else{

            return "redirect:/city/all-city";
        }
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editCity(@PathVariable int id, Model model ){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Update City");

        model.addAttribute("addNewCity", cityService.getCity(id));

        return "admin/city/new";
    }

    @RequestMapping(value = "/all-city", method = RequestMethod.GET)
    public String allCity(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        publisherAndUser.publisherAndUser(model);

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<City> cityList;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "name");


        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            cityList = cityService.getAllCity(pageable);
        }
        else {

            cityList = cityService.getAllCityWithSearchKey(searchKey, pageable);
        }

        int totalElements = (int) cityList.getTotalElements();

        if (totalElements % elementPerPage == 0) {
            numberOfPage = totalElements / elementPerPage;
        } else if (totalElements % elementPerPage > 0) {
            numberOfPage = (totalElements / elementPerPage) + 1;
        }

        model.addAttribute("page_title", "All Cities");
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("cityList", cityList);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("cityNames", sortedCity());
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "admin/city/all";
    }

    private List<City> sortedCity(){

        List<City> citySortedList = cityService.getAllCity();
        Collections.sort(citySortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        return citySortedList;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchCity(@RequestParam("term") String searchKey){

        List<City> citySortedList = cityService.getAllCityWithKey(searchKey);
        Collections.sort(citySortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        List<String> names = new ArrayList<>();

        for (City city: citySortedList){
            names.add(city.getName());
        }

        return names;
    }

    @RequestMapping(value = "/city-detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public City cityDetail(@PathVariable int id){

        return cityService.getCity(id);
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteCity(@PathVariable int id, RedirectAttributes redirectAttributes){

        cityService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","city Deleted");

        return "redirect:/city/all-city";
    }

    private void clearTextBoxes(City city){

        city.setName(null);
    }
}
