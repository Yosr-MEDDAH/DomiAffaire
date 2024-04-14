package com.domiaffaire.api.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinimumAgeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinimumAge {
    String message() default "Age must be 18 years or older";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
