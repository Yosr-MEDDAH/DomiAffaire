package com.domiaffaire.api.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class NoBadWordsValidator implements ConstraintValidator<NoBadWords, String> {

    private List<String> badWords;

    @Override
    public void initialize(NoBadWords constraintAnnotation) {
        badWords = Arrays.asList(
                "merde", "putain", "con", "connard", "salope", "enculé", "fils de pute",
                "pute", "bordel", "ta gueule", "nique", "niquer", "cul", "chier", "chiant",
                "pédé", "tapette", "enculer", "enculer", "fdp", "ntm", "tg", "trou du cul",
                "bite", "couilles", "chatte", "nique ta mère", "pute", "pd", "enfoiré",
                "connasse", "gouine", "bâtard", "gros con", "grosse merde", "pute",
                "nique ta race", "ta mère", "sac à foutre"
        );;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }


        for (String badWord : badWords) {
            if (value.toLowerCase().contains(badWord.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}