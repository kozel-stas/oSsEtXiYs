package com.oSsEtXiYs.rest;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UrlValidator.class)
public @interface UrlValid {
    String message() default "Url can access by http and https protocols only.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
