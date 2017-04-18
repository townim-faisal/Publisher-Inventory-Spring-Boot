package rokomari.PublisherInventory.service.admin.annotations.publisher;

@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD,})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@javax.validation.Constraint(validatedBy = {PublisherPhoneDigitValidator.class})
public @interface PublisherPhoneDigitValidate {

    String message();

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};
}
