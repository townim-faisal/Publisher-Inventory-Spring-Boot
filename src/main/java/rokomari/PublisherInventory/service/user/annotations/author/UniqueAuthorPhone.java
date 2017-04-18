package rokomari.PublisherInventory.service.user.annotations.author;

import java.lang.annotation.ElementType;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;

@java.lang.annotation.Target({ElementType.TYPE,ANNOTATION_TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@javax.validation.Constraint(validatedBy = {UniqueAuthorPhoneValidator.class})
public @interface UniqueAuthorPhone {

    java.lang.String message();

    java.lang.Class<?>[] groups() default {};

    java.lang.Class<? extends javax.validation.Payload>[] payload() default {};
}
