//This Class is not in use


package rokomari.PublisherInventory.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/role")
public class EndUserRoleController {


    /*@Autowired
    UserRoleService userRoleService;*/

    /*@RequestMapping(value="/new-role", method = RequestMethod.GET)
    public String newUser(Model model){

        UserRole roleUser = new UserRole();
        UserRole roleAdmin = new UserRole();
        UserRole roleSuperAdmin = new UserRole();

        roleUser.setRole("USER");
        roleAdmin.setRole("ADMIN");
        *//*roleSuperAdmin.setRole("SUPER_ADMIN");*//*

        userRoleService.create(roleUser);
        userRoleService.create(roleAdmin);
        *//*userRoleService.create(roleSuperAdmin);*//*

        return "admin/role/new";
    }*/

    @RequestMapping(value="/new-role", method = RequestMethod.GET)
    @ResponseBody
    public String newUser(Model model){



        return "This method and associated controller has no use because user roles are created during first Super user creation.";
    }



    /*@RequestMapping(value="/new-role", method = RequestMethod.POST)
    public String newUser(@Valid @ModelAttribute("addNewRole") Author author, BindingResult bindingResult,
                          Model model, RedirectAttributes redirectAttributes){

        EndUser endUser = new EndUser();
        model.addAttribute("addNewRole", endUser);

        return "user/new";
    }*/
/*

    @RequestMapping(value="/new-user", method = RequestMethod.POST)
    public String newUser(@Valid @ModelAttribute("addNewEndUser") Author author, BindingResult bindingResult,
                          Model model, RedirectAttributes redirectAttributes){


        EndUser admin = new EndUser();
        EndUser user = new EndUser();
        EndUser adminuser = new EndUser();

        admin.setName("admin");
        admin.setPassword("pass");

        user.setName("user");
        user.setPassword("pass");

        adminuser.setName("adminuser");
        adminuser.setPassword("pass");

        endUserService.createEndUser(admin);
        endUserService.createEndUser(user);
        endUserService.createEndUser(adminuser);


        UserRole adminRole = new UserRole();
        UserRole userRole = new UserRole();

        adminRole.setRole("ADMIN");
        userRole.setRole("USER");

        userRoleService.createEndUser(adminRole);
        userRoleService.createEndUser(userRole);


        // Assign Role
        admin.getRoles().add(adminRole);
        user.getRoles().add(userRole);
        adminuser.getRoles().add(adminRole);
        adminuser.getRoles().add(userRole);


        endUserService.createEndUser(admin); // This single instance assigns roles for all endUser if declared in "Assign Role" block

        return "newuser";
    }
*/

/*

    // This method is serving both Create and Update operation
    @RequestMapping(value="/new-user", method = RequestMethod.POST)
    public String newUser(@Valid @ModelAttribute("addNewEndUser") EndUser endUser, BindingResult bindingResult,
                                  Model model, RedirectAttributes redirectAttributes){

        int currentId = endUser.getId(); // Storing the old value
        String currentName = endUser.getName();

        model.addAttribute("addNewEndUser", endUser);

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");

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
            endUserService.createUser(endUser); // this operation changes the id number after creating new entry
            clearTextBoxes(endUser);

            endUser.setId(currentId); // setting the old value
            currentId=endUser.getId();

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
*/

    /*@RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editAuthor(@PathVariable int id, Model model ){

        model.addAttribute("addNewEndUser", endUserService.getUser(id));

        return "newuser";
    }*/


    /*@RequestMapping(value = "/all-user", method = RequestMethod.GET)
    public String allAuthor(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<EndUser> users;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "name");


        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            users = endUserService.getAllUsers(pageable);
        }
        else {

            users = endUserService.getAllUserWithSearchKey(searchKey, pageable);
        }

            int totalElements = (int) users.getTotalElements();

            if (totalElements % elementPerPage == 0) {
                numberOfPage = totalElements / elementPerPage;
            } else if (totalElements % elementPerPage > 0) {
                numberOfPage = (totalElements / elementPerPage) + 1;
            }

        model.addAttribute("users", users);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("userNames", sortedUsers());
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "allusers";
    }*/

    /*private List<EndUser> sortedUsers(){

        List<EndUser> usersSortedList = endUserService.getAllUser();
        Collections.sort(usersSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        return usersSortedList;
    }*/

    /*@RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchUser(@RequestParam("term") String searchKey){

        List<EndUser> usersSortedList = endUserService.getAllUserWithKey(searchKey);
        Collections.sort(usersSortedList,(a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        List<String> names = new ArrayList<>();

        for (EndUser user: usersSortedList){
            names.add(user.getName());
        }

        return names;
    }*/

    /*@RequestMapping(value = "/author-detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Author authorDetail(@PathVariable int id){

        return authorService.getAuthor(id);
    }*/

    /*@RequestMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable int id, RedirectAttributes redirectAttributes){

        endUserService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","User Deleted");

        return "redirect:/user/all-user";
    }*/

    /*@RequestMapping("/isPhoneExist")
    @ResponseBody
    public String isPhoneExist(@RequestParam String phone){

        try {

            boolean bool = authorService.getAuthorByPhone(phone) == null;
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
            if(authorService.getAuthorByPhone(phone).getId() == id || authorService.getAuthorByPhone(phone) == null){

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

    /*private String autoGenerateCode(String name){

        return "Test Autogenerated Code" + name;
    }*/

    /*private void clearTextBoxes(EndUser user){

        user.se(null);
        user.setAddress(null);
        user.setEmail(null);
    }*/
}
