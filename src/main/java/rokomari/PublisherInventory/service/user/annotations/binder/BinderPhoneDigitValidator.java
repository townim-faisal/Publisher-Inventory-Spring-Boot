package rokomari.PublisherInventory.service.user.annotations.binder;




import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BinderPhoneDigitValidator implements ConstraintValidator <BinderPhoneDigitValidate, String>{

    @Override
    public void initialize(BinderPhoneDigitValidate authorPhoneDigitValidate) {

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
