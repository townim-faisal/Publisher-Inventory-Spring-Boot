package rokomari.PublisherInventory.service.user.annotations.customer;




import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomerPhoneDigitValidator implements ConstraintValidator <CustomerPhoneDigitValidate, String>{

    @Override
    public void initialize(CustomerPhoneDigitValidate customerPhoneDigitValidate) {

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
