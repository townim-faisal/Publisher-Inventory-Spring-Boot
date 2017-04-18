package rokomari.PublisherInventory.service.admin.annotations.publisher;

import java.lang.annotation.ElementType;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;

@java.lang.annotation.Target({ElementType.TYPE,ANNOTATION_TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@javax.validation.Constraint(validatedBy = {UniquePublisherPhoneValidator.class})
public @interface UniquePublisherPhone {

    String message();

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};
}
