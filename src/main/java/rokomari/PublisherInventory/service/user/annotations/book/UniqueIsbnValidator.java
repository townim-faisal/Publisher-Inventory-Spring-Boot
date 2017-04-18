package rokomari.PublisherInventory.service.user.annotations.book;


import org.springframework.beans.factory.annotation.Autowired;
import rokomari.PublisherInventory.service.user.services.BookService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueIsbnValidator implements ConstraintValidator <UniqueIsbn, String>{

    @Autowired
    private BookService bookService;
    @Override
    public void initialize(UniqueIsbn uniqueIsbn) {

    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {

        if (bookService == null){
            return true;
        }

//        return  bookService.findByIsbn(isbn)== null;
        return  false;
    }
}
