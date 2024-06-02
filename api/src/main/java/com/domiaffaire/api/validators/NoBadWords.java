package com.domiaffaire.api.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoBadWordsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoBadWords {
    String message() default "Input contains bad words";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}