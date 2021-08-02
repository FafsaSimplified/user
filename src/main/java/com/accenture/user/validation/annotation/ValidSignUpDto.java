package com.accenture.user.validation.annotation;

import com.accenture.user.validation.LoginInfoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {LoginInfoValidator.class})
public @interface ValidSignUpDto {
    String message() default "Error messages in case the constraint is violated";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
