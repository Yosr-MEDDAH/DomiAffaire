package com.domiaffaire.api.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class NoBadWordsValidator implements ConstraintValidator<NoBadWords, String> {

    private List<String> badWords;

    @Override
    public void initialize(NoBadWords constraintAnnotation) {
        // Load bad words from your dataset here
        // You can load it from a CSV file or any other source
        // For demonstration, I'll initialize it with some sample bad words
        badWords = Arrays.asList("miboun", "ta7an", "badword3");
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are considered valid
        }

        // Check if the value contains any of the bad words
        for (String badWord : badWords) {
            if (value.toLowerCase().contains(badWord.toLowerCase())) {
                return false; // Found a bad word, validation fails
            }
        }
        return true; // No bad words found, validation passes
    }
}