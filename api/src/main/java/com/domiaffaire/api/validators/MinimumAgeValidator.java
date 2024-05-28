package com.domiaffaire.api.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class MinimumAgeValidator implements ConstraintValidator<MinimumAge, Date> {
    @Override
    public void initialize(MinimumAge constraintAnnotation) {
    }

    @Override
    public boolean isValid(Date birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return true;
        }

        LocalDate today = LocalDate.now();
        LocalDate dob = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int age = Period.between(dob, today).getYears();

        return age >= 18;
    }
}