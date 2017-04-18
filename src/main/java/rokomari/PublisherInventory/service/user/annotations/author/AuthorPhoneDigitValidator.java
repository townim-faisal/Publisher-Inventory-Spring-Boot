package rokomari.PublisherInventory.service.user.annotations.author;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AuthorPhoneDigitValidator implements ConstraintValidator <AuthorPhoneDigitValidate, String>{

    @Override
    public void initialize(AuthorPhoneDigitValidate authorPhoneDigitValidate) {

    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {

        if (phone.matches("\\d+")){
            return true;
        }
        else {
            return false;
        }
    }
}
