package com.ms.spring.starter.validation;

import com.ms.spring.starter.dto.ChangePasswordRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, ChangePasswordRequest> {

    @Override
    public boolean isValid(ChangePasswordRequest dto, ConstraintValidatorContext context) {
        if (dto.getPassword() == null || dto.getConfirmPassword() == null) {
            return false;
        }
        return dto.getPassword().equals(dto.getConfirmPassword());
    }
}
