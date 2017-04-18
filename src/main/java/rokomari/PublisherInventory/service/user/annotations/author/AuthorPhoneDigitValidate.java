package rokomari.PublisherInventory.service.user.annotations.author;

@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD,})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@javax.validation.Constraint(validatedBy = {AuthorPhoneDigitValidator.class})
public @interface AuthorPhoneDigitValidate {

    String message();

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};
}
