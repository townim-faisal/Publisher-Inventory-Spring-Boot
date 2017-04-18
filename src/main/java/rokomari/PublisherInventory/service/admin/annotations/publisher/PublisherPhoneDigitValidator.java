package rokomari.PublisherInventory.service.admin.annotations.publisher;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PublisherPhoneDigitValidator implements ConstraintValidator <PublisherPhoneDigitValidate, String>{

    @Override
    public void initialize(PublisherPhoneDigitValidate publisherPhoneDigitValidate) {

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
