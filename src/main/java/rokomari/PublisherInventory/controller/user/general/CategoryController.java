package rokomari.PublisherInventory.controller.user.general;

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
import rokomari.PublisherInventory.model.user.general.Category;
import rokomari.PublisherInventory.service.common.PublisherAndUser;
import rokomari.PublisherInventory.service.admin.services.EndUserService;
import rokomari.PublisherInventory.service.user.services.CategoryService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController extends WebMvcConfigurerAdapter {

    @Autowired
    CategoryService categoryService;

    @Autowired
    EndUserService endUserService;

    @Autowired
    PublisherAndUser publisherAndUser;

    private Publisher getCurrentPublisher(){

        return publisherAndUser.getCurrentPublisher();
    }

    @RequestMapping(value="/new-category", method = RequestMethod.GET)
    public String newCategory(Model model){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Add Category");

        Category category = new Category();
        category.setPublisher(getCurrentPublisher());
        model.addAttribute("addNewCategory", category);

        return "user/category/new";
    }

    @RequestMapping(value="/new-category", method = RequestMethod.POST)
    public String newCategory(@Valid @ModelAttribute("addNewCategory") Category category, BindingResult bindingResult,
                              Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model);

        int currentId = category.getId(); // Storing the old value
        String currentName = category.getName();

        model.addAttribute("addNewCategory", category);

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");
            /*System.out.println("Error : "+bindingResult.getAllErrors());*/
            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added");

                return "redirect:/category/new-category";
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");

                return "redirect:/category/edit/" + currentId;
            }
        }

        try{

            if(isCategoryExist(category.getName(), currentId).equals("true")){
                categoryService.createCategory(category); // this operation changes the id number after creating new entry
                clearTextBoxes(category);
            }
            else{
                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added or updated");

                return "redirect:/category/new-category";
            }

            category.setId(currentId); // setting the old value
            currentId=category.getId();

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
                return "redirect:/category/new-category";

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");
                return "redirect:/category/edit/" + currentId;
            }
        }

        if(currentId == 0){

            return "redirect:/category/new-category";
        }

        else{

            return "redirect:/category/all-category";
        }
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editCategory(@PathVariable int id, Model model ){

        publisherAndUser.publisherAndUser(model);

        model.addAttribute("page_title", "Update Category");

        model.addAttribute("addNewCategory", categoryService.getCategory(getCurrentPublisher(), id));

        return "user/category/new";
    }

    @RequestMapping(value = "/all-category", method = RequestMethod.GET)
    public String allCategory(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        publisherAndUser.publisherAndUser(model);

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<Category> categoryList;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "name");


        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            categoryList = categoryService.getAllCategories(getCurrentPublisher(),pageable);
        }
        else {

            categoryList = categoryService.getAllCategoryWithSearchKey(getCurrentPublisher(),searchKey, pageable);
        }

        int totalElements = (int) categoryList.getTotalElements();
        if (totalElements % elementPerPage == 0) {
            numberOfPage = totalElements / elementPerPage;
        } else if (totalElements % elementPerPage > 0) {
            numberOfPage = (totalElements / elementPerPage) + 1;
        }

        model.addAttribute("page_title", "All Categories");
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("categoryNames",sortedCategories(getCurrentPublisher()));
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "user/category/all";
    }

    private List<Category> sortedCategories(Publisher publisher){

        List<Category> categoriesSortedList = categoryService.getAllCategory(publisher);
        Collections.sort(categoriesSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        return categoriesSortedList;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchCategory(@RequestParam("term") String searchKey){

        List<Category> categoriesSortedList = categoryService.getAllCategoryWithKey(getCurrentPublisher(), searchKey);
        Collections.sort(categoriesSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        List<String> names = new ArrayList<>();

        for (Category category: categoriesSortedList){
            names.add(category.getName());
        }

        return names;
    }

    @RequestMapping(value = "/category-detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Category categoryDetail(@PathVariable int id){

        return categoryService.getCategory(getCurrentPublisher(),id);
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteCategory(@PathVariable int id, RedirectAttributes redirectAttributes){

        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","Category Deleted");

        return "redirect:/category/all-category";
    }



    @RequestMapping("/isCategoryExist")
    @ResponseBody
    public String isCategoryExist(@RequestParam String name, @RequestParam int id){

        try{
            if(categoryService.getCategoryByName(getCurrentPublisher(), name).getId() == id || categoryService.getCategoryByName(getCurrentPublisher(), name) == null){

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

    private void clearTextBoxes(Category category){

        category.setName(null);
    }
}
