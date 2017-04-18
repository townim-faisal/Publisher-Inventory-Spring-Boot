package rokomari.PublisherInventory.service.user.annotations.binder;

import rokomari.PublisherInventory.service.user.annotations.author.AuthorPhoneDigitValidator;

@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD,})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@javax.validation.Constraint(validatedBy = {BinderPhoneDigitValidator.class})
public @interface BinderPhoneDigitValidate {

    String message();

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};
}
