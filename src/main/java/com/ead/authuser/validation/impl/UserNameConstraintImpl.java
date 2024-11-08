package com.ead.authuser.validation.impl;

import com.ead.authuser.validation.UserNameConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserNameConstraintImpl implements ConstraintValidator<UserNameConstraint, String> {
    @Override
    public void initialize(UserNameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {

        if(username.isBlank() || username.trim().isEmpty() || username.contains(" "))
            return false;

        return true;
    }
}
