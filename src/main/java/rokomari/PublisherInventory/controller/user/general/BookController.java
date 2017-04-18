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
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.general.Book;
import rokomari.PublisherInventory.service.common.PublisherAndUser;
import rokomari.PublisherInventory.service.user.services.AuthorService;
import rokomari.PublisherInventory.service.user.services.BookService;
import rokomari.PublisherInventory.service.user.services.CategoryService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController{

    @Autowired
    BookService bookService;

    @Autowired
    AuthorService authorService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    PublisherAndUser publisherAndUser;

    private Publisher getCurrentPublisher(){

        return publisherAndUser.getCurrentPublisher();
    }

    //    @PreBookize("hasBookity('ADMIN')")
    @RequestMapping(value="/new-book", method = RequestMethod.GET)
    public String newBook(Model model){

        publisherAndUser.publisherAndUser(model);
        model.addAttribute("page_title", "Add Book");

        Book book = new Book();

        Publisher publisher = getCurrentPublisher();

        book.setPublisher(publisher);
        model.addAttribute("addNewBook", book);
        model.addAttribute("allAuthor", authorService.getAllAuthors(publisher));
        model.addAttribute("allCategory",categoryService.getAllCategory(publisher));

        return "user/book/new";
    }

    // This method is serving both Create and Update operation

    @RequestMapping(value="/new-book", method = RequestMethod.POST)
    public String newBook(@Valid @ModelAttribute("addNewBook") Book book, BindingResult bindingResult,
                            Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model);

        int currentId = book.getId(); // Storing the old value
        String currentName = book.getName();

        model.addAttribute("addNewBook", book);

        System.out.println("================== Author none: book will not save"+book.getAuthors());
        System.out.println("================== Category none: book will not save"+book.getAuthors());


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");
            System.out.println("Error in Binding Result: "+bindingResult.getAllErrors());
            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added");

                return "redirect:/book/new-book";
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");

                return "redirect:/book/edit/" + currentId;
            }
        }

        try{

            if(isIsbnExist(book.getIsbn(),currentId).equals("true") || book.getDiscount() <= book.getCoverPrice()){
                book.setSellPrice(book.getCoverPrice()- book.getDiscount());
                bookService.createBook(book); // this operation changes the id number after creating new entry
                clearTextBoxes(book);
            }
            else{
                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be added or updated");

                return "redirect:/book/new-book";
            }

            book.setId(currentId); // setting the old value
            currentId=book.getId();

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
                return "redirect:/book/new-book";

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",currentName+" can not be updated");
                return "redirect:/book/edit/" + currentId;
            }
        }

        if(currentId == 0){

            return "redirect:/book/new-book";
        }

        else{

            return "redirect:/book/all-book";
        }
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editBook(@PathVariable int id, Model model ){

        /*model.addAttribute("publisher_name_navbar", getPublisherName());*/
        publisherAndUser.publisherAndUser(model);

        Publisher publisher = getCurrentPublisher();

        Book book = bookService.getBook(publisher, id);

        model.addAttribute("page_title", "Update Book");
        model.addAttribute("author",book.getAuthors());
        model.addAttribute("category",book.getCategories());
        model.addAttribute("allAuthor", authorService.getAllAuthors(publisher));
        model.addAttribute("allCategory",categoryService.getAllCategory(publisher));
        model.addAttribute("addNewBook", book);

        return "user/book/new";
    }


    @RequestMapping(value = "/all-book", method = RequestMethod.GET)
    public String allBook(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        publisherAndUser.publisherAndUser(model);

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<Book> bookList;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "name");


        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            bookList = bookService.getAllBooks(getCurrentPublisher(),pageable);
        }
        else {

            bookList = bookService.getAllBookWithSearchKey(getCurrentPublisher(),searchKey, pageable);
        }

        int totalElements = (int) bookList.getTotalElements();
        if (totalElements % elementPerPage == 0) {
            numberOfPage = totalElements / elementPerPage;
        } else if (totalElements % elementPerPage > 0) {
            numberOfPage = (totalElements / elementPerPage) + 1;
        }

        model.addAttribute("page_title", "All Books");
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("bookList", bookList);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("bookNames",sortedBooks(getCurrentPublisher()));
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "user/book/all";
    }

    private List<Book> sortedBooks(Publisher publisher){

        List<Book> booksSortedList = bookService.getAllBooks(publisher);
        Collections.sort(booksSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        return booksSortedList;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchBook(@RequestParam("term") String searchKey){

        List<Book> booksSortedList = bookService.getAllBookWithKey(getCurrentPublisher(), searchKey);
        Collections.sort(booksSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        List<String> names = new ArrayList<>();

        for (Book book: booksSortedList){
            names.add(book.getName());
        }

        return names;
    }

    @RequestMapping(value = "/book-detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Book bookDetail(@PathVariable int id){

        return bookService.getBook(getCurrentPublisher(),id);
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteBook(@PathVariable int id, RedirectAttributes redirectAttributes){

        bookService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","Book Deleted");

        return "redirect:/book/all-book";
    }



    @RequestMapping("/isIsbnExist")
    @ResponseBody
    public String isIsbnExist(@RequestParam String isbn, @RequestParam int id){

        try{
            if(bookService.getBookByIsbn(getCurrentPublisher(), isbn).getId() == id || bookService.getBookByIsbn(getCurrentPublisher(), isbn) == null){

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



    private void clearTextBoxes(Book book){

       /* book.setPhone(null);
        book.setFacebook(null);
        book.setDateOfBirth(null);
        book.setName(null);
        book.setAddress(null);
        book.setEmail(null);*/
    }
}
