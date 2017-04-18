package rokomari.PublisherInventory.controller.user.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.inventory.Inventory;
import rokomari.PublisherInventory.service.admin.services.EndUserService;
import rokomari.PublisherInventory.service.common.PublisherAndUser;
import rokomari.PublisherInventory.service.user.services.InventoryService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/inventory")
public class InventoryController extends WebMvcConfigurerAdapter {

    @Autowired
    InventoryService inventoryService;

    @Autowired
    EndUserService endUserService;

    @Autowired
    PublisherAndUser publisherAndUser;

    private Publisher getCurrentPublisher(){

        return publisherAndUser.getCurrentPublisher();
    }

    @RequestMapping(value="/new-inventory", method = RequestMethod.GET)
    public String newInventory(Model model){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Add Inventory");

        Inventory inventory = new Inventory();
        inventory.setPublisher(getCurrentPublisher());
        model.addAttribute("addNewInventory", inventory);

        return "user/inventory/new";
    }

    @RequestMapping(value="/new-inventory", method = RequestMethod.POST)
    public String newInventory(@Valid @ModelAttribute("addNewInventory") Inventory inventory, BindingResult bindingResult,
                              Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model);

        int currentId = inventory.getId(); // Storing the old value
        String currentName = inventory.getName();

        model.addAttribute("addNewInventory", inventory);

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");
            /*System.out.println("Error : "+bindingResult.getAllErrors());*/
            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added");

                return "redirect:/inventory/new-inventory";
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");

                return "redirect:/inventory/edit/" + currentId;
            }
        }

        try{

            if(isInventoryExist(inventory.getName(), currentId).equals("true")){
                inventoryService.createInventory(inventory); // this operation changes the id number after creating new entry
                clearTextBoxes(inventory);
            }
            else{
                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added or updated");

                return "redirect:/inventory/new-inventory";
            }

            inventory.setId(currentId); // setting the old value
            currentId=inventory.getId();

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
                return "redirect:/inventory/new-inventory";

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");
                return "redirect:/inventory/edit/" + currentId;
            }
        }

        if(currentId == 0){

            return "redirect:/inventory/new-inventory";
        }

        else{

            return "redirect:/inventory/all-inventory";
        }
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editInventory(@PathVariable int id, Model model ){

        publisherAndUser.publisherAndUser(model);

        model.addAttribute("page_title", "Update Inventory");

        model.addAttribute("addNewInventory", inventoryService.getInventory(getCurrentPublisher(), id));

        return "user/inventory/new";
    }

    @RequestMapping(value = "/all-inventory", method = RequestMethod.GET)
    public String allInventory(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        publisherAndUser.publisherAndUser(model);

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<Inventory> inventoryList;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "name");


        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            inventoryList = inventoryService.getAllInventories(getCurrentPublisher(),pageable);
        }
        else {

            inventoryList = inventoryService.getAllInventoryWithSearchKey(getCurrentPublisher(),searchKey, pageable);
        }

        int totalElements = (int) inventoryList.getTotalElements();
        if (totalElements % elementPerPage == 0) {
            numberOfPage = totalElements / elementPerPage;
        } else if (totalElements % elementPerPage > 0) {
            numberOfPage = (totalElements / elementPerPage) + 1;
        }

        model.addAttribute("page_title", "All Inventories");
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("inventoryList", inventoryList);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("inventoryNames",sortedCategories(getCurrentPublisher()));
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "user/inventory/all";
    }

    private List<Inventory> sortedCategories(Publisher publisher){

        List<Inventory> categoriesSortedList = inventoryService.getAllInventory(publisher);
        Collections.sort(categoriesSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        return categoriesSortedList;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchInventory(@RequestParam("term") String searchKey){

        List<Inventory> categoriesSortedList = inventoryService.getAllInventoryWithKey(getCurrentPublisher(), searchKey);
        Collections.sort(categoriesSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        List<String> names = new ArrayList<>();

        for (Inventory inventory: categoriesSortedList){
            names.add(inventory.getName());
        }

        return names;
    }

    @RequestMapping(value = "/inventory-detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Inventory inventoryDetail(@PathVariable int id){

        return inventoryService.getInventory(getCurrentPublisher(),id);
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteInventory(@PathVariable int id, RedirectAttributes redirectAttributes){

        inventoryService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","Inventory Deleted");

        return "redirect:/inventory/all-inventory";
    }



    @RequestMapping("/isInventoryExist")
    @ResponseBody
    public String isInventoryExist(@RequestParam String name, @RequestParam int id){

        try{
            if(inventoryService.getInventoryByName(getCurrentPublisher(), name).getId() == id || inventoryService.getInventoryByName(getCurrentPublisher(), name) == null){

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

    private void clearTextBoxes(Inventory inventory){

        inventory.setName(null);
    }
}
