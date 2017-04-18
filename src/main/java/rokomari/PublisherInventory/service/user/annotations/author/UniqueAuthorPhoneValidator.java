package rokomari.PublisherInventory.service.user.annotations.author;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.general.Author;
import rokomari.PublisherInventory.service.admin.services.EndUserService;
import rokomari.PublisherInventory.service.user.services.AuthorService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueAuthorPhoneValidator implements ConstraintValidator <UniqueAuthorPhone, Author>{

    @Autowired
    private AuthorService authorService;

    @Autowired
    EndUserService endUserService;

    @Override
    public void initialize(UniqueAuthorPhone uniqueAuthorPhone) {

    }

    private Publisher getCurrentPublisher(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return endUserService.getUserByUserName(authentication.getName()).getPublisher();
    }

    @Override
    public boolean isValid(Author author, ConstraintValidatorContext constraintValidatorContext) {

        try{
            if(authorService.getAuthorByPhone(getCurrentPublisher(), author.getPhone()).getId() == author.getId() || authorService.getAuthorByPhone(getCurrentPublisher(), author.getPhone()) == null){

                System.out.println("Return ID: "+ authorService.getAuthorByPhone(getCurrentPublisher(), author.getPhone()).getId());

                return true;

                //return false;
            }
            else {

                System.out.println("FALSE, ID: "+authorService.getAuthorByPhone(getCurrentPublisher(), author.getPhone()).getId());
                System.out.println("Other ID: "+author.getId());

                return false;

                //return true;
            }
        }
        catch (Exception e){

            return true;

            //return false;
        }
    }
}
