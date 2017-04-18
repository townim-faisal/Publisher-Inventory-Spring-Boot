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
import rokomari.PublisherInventory.model.common.Area;
import rokomari.PublisherInventory.service.admin.services.AreaService;
import rokomari.PublisherInventory.service.common.PublisherAndUser;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/area")
public class AreaController {


    @Autowired
    AreaService areaService;

    @Autowired
    PublisherAndUser publisherAndUser;

    @RequestMapping(value="/new-area", method = RequestMethod.GET)
    public String newUser(Model model){

        publisherAndUser.publisherAndUser(model);

        model.addAttribute("page_title", "Add Area");

        model.addAttribute("addNewArea",new Area());

        return "admin/area/new";
    }

    @RequestMapping(value="/new-area", method = RequestMethod.POST)
    public String newArea(@Valid @ModelAttribute("addNewArea") Area area, BindingResult bindingResult,
                               Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model);

        model.addAttribute("page_title", "Add Area");

        int currentId = area.getId(); // Storing the old value
        String currentName = area.getName();

        model.addAttribute("addNewArea", area);

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");

            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added");

                return "redirect:/area/new-area";
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");

                return "redirect:/area/edit/" + currentId;
            }
        }

        try{
            areaService.createArea(area); // this operation changes the id number after creating new entry
            clearTextBoxes(area);

            area.setId(currentId); // setting the old value
            currentId = area.getId();

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
                return "redirect:/area/new-area";

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");
                return "redirect:/area/edit/" + currentId;
            }
        }

        if(currentId == 0){

            return "redirect:/area/new-area"; //redirects to method, not html page
        }

        else{

            return "redirect:/area/all-area";
        }
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editArea(@PathVariable int id, Model model ){

        publisherAndUser.publisherAndUser(model);

        model.addAttribute("page_title", "Update Area");

        model.addAttribute("addNewArea", areaService.getArea(id));

        return "admin/area/new";
    }

    @RequestMapping(value = "/all-area", method = RequestMethod.GET)
    public String allArea(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        publisherAndUser.publisherAndUser(model);

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<Area> areaList;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "name");


        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            areaList = areaService.getAllArea(pageable);
        }
        else {

            areaList = areaService.getAllAreaWithSearchKey(searchKey, pageable);
        }

        int totalElements = (int) areaList.getTotalElements();

        if (totalElements % elementPerPage == 0) {
            numberOfPage = totalElements / elementPerPage;
        } else if (totalElements % elementPerPage > 0) {
            numberOfPage = (totalElements / elementPerPage) + 1;
        }

        model.addAttribute("page_title", "All Areas");
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("areaList", areaList);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("areaNames", sortedArea());
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "admin/area/all";
    }

    private List<Area> sortedArea(){

        List<Area> areaSortedList = areaService.getAllArea();
        Collections.sort(areaSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        return areaSortedList;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchArea(@RequestParam("term") String searchKey){

        List<Area> areaSortedList = areaService.getAllAreaWithKey(searchKey);
        Collections.sort(areaSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        List<String> names = new ArrayList<>();

        for (Area area: areaSortedList){
            names.add(area.getName());
        }

        return names;
    }

    @RequestMapping(value = "/area-detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Area areaDetail(@PathVariable int id){

        return areaService.getArea(id);
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteArea(@PathVariable int id, RedirectAttributes redirectAttributes){

        areaService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","area Deleted");

        return "redirect:/area/all-area";
    }

    private void clearTextBoxes(Area area){

        area.setName(null);
    }
}
