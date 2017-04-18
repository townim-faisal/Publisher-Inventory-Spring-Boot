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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rokomari.PublisherInventory.service.admin.services.AreaService;
import rokomari.PublisherInventory.service.admin.services.CityService;
import rokomari.PublisherInventory.service.admin.services.CountryService;
import rokomari.PublisherInventory.service.common.PublisherAndUser;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.general.Author;
import rokomari.PublisherInventory.service.user.services.AuthorService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    AuthorService authorService;

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


//    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value="/new-author", method = RequestMethod.GET)
    public String newAuthor(Model model){

        publisherAndUser.publisherAndUser(model);


        Author author = new Author();

        author.setPublisher(getCurrentPublisher());
        model.addAttribute("page_title", "Add Author");
        model.addAttribute("addNewAuthor", author);
        model.addAttribute("allCity", cityService.getAllCity());
        model.addAttribute("allArea",areaService.getAllArea());
        model.addAttribute("allCountry",countryService.getAllCountry());

        return "user/author/new";
    }

    // This method is serving both Create and Update operation

    @RequestMapping(value="/new-author", method = RequestMethod.POST)
    public String newAuthor(@Valid @ModelAttribute("addNewAuthor") Author author, BindingResult bindingResult,
                                  Model model, RedirectAttributes redirectAttributes){


        publisherAndUser.publisherAndUser(model);

        int currentId = author.getId(); // Storing the old value
        String currentName = author.getName();

        model.addAttribute("addNewAuthor", author);
        model.addAttribute("page_title", "Add Author");

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");
            /*System.out.println("Error : "+bindingResult.getAllErrors());*/
            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added");

                return "redirect:/author/new-author";
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");

                return "redirect:/author/edit/" + currentId;
            }
        }

        try{

            if(isPhoneExist(author.getPhone(),currentId).equals("true") || isEmailExist(author.getEmail(), currentId).equals("true")){
                authorService.createAuthor(author); // this operation changes the id number after creating new entry
                clearTextBoxes(author);
            }
            else{
                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added or updated");

                return "redirect:/author/new-author";
            }

            author.setId(currentId); // setting the old value
            currentId=author.getId();

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
                return "redirect:/author/new-author";

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");
                return "redirect:/author/edit/" + currentId;
            }
        }

        if(currentId == 0){

            return "redirect:/author/new-author";
        }

        else{

            return "redirect:/author/all-author";
        }
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editAuthor(@PathVariable int id, Model model ){

        publisherAndUser.publisherAndUser(model);

        Author author = authorService.getAuthor(getCurrentPublisher(), id);

        model.addAttribute("page_title", "Update Author");
        model.addAttribute("city",author.getAddress().getCity());
        model.addAttribute("area",author.getAddress().getArea());
        model.addAttribute("country",author.getAddress().getCountry());
        model.addAttribute("allCity", cityService.getAllCity());
        model.addAttribute("allArea",areaService.getAllArea());
        model.addAttribute("allCountry",countryService.getAllCountry());
        model.addAttribute("addNewAuthor", author);

        return "user/author/new";
    }


    @RequestMapping(value = "/all-author", method = RequestMethod.GET)
    public String allAuthor(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        publisherAndUser.publisherAndUser(model);

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<Author> authorList;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "name");


        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            authorList = authorService.getAllAuthors(getCurrentPublisher(),pageable);
        }
        else {

            authorList = authorService.getAllAuthorWithSearchKey(getCurrentPublisher(),searchKey, pageable);
        }

        int totalElements = (int) authorList.getTotalElements();
        if (totalElements % elementPerPage == 0) {
            numberOfPage = totalElements / elementPerPage;
        } else if (totalElements % elementPerPage > 0) {
            numberOfPage = (totalElements / elementPerPage) + 1;
        }

        model.addAttribute("page_title", "All Authors");
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("authorList", authorList);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("authorNames",sortedAuthors(getCurrentPublisher()));
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "user/author/all";
    }

    private List<Author> sortedAuthors(Publisher publisher){

        List<Author> authorsSortedList = authorService.getAllAuthors(publisher);
        Collections.sort(authorsSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        return authorsSortedList;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchAuthor(@RequestParam("term") String searchKey){

        List<Author> authorsSortedList = authorService.getAllAuthorWithKey(getCurrentPublisher(), searchKey);
        Collections.sort(authorsSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        List<String> names = new ArrayList<>();

        for (Author author: authorsSortedList){
            names.add(author.getName());
        }

        return names;
    }

    @RequestMapping(value = "/author-detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Author authorDetail(@PathVariable int id){

        return authorService.getAuthor(getCurrentPublisher(),id);
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteAuthor(@PathVariable int id, RedirectAttributes redirectAttributes){

        authorService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","Author Deleted");

        return "redirect:/author/all-author";
    }



    @RequestMapping("/isEmailExist")
    @ResponseBody
    public String isEmailExist(@RequestParam String email, @RequestParam int id){

        try{
            if(authorService.getAuthorByEmail(getCurrentPublisher(), email).getId() == id || authorService.getAuthorByEmail(getCurrentPublisher(), email) == null){

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
            if(authorService.getAuthorByPhone(getCurrentPublisher(), phone).getId() == id || authorService.getAuthorByPhone(getCurrentPublisher(), phone) == null){

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

    private void clearTextBoxes(Author author){

        author.setPhone(null);
        author.setFacebook(null);
        author.setDateOfBirth(null);
        author.setName(null);
        author.setAddress(null);
        author.setEmail(null);
    }
}
