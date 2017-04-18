package rokomari.PublisherInventory.service.user.annotations.customer;

@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD,})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@javax.validation.Constraint(validatedBy = {CustomerPhoneDigitValidator.class})
public @interface CustomerPhoneDigitValidate {

    String message();

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};
}
