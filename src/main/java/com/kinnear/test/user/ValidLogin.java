package com.kinnear.test.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidLoginValidator.class)
public @interface ValidLogin {

    String message() default "{com.kinnear.test.ValidLogin.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
