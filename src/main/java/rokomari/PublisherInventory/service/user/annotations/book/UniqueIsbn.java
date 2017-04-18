package rokomari.PublisherInventory.service.user.annotations.book;



@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD,})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@javax.validation.Constraint(validatedBy = {UniqueIsbnValidator.class})
public @interface UniqueIsbn {

    String message();

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};
}
