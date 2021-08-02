package com.accenture.user.validation;

import com.accenture.user.dto.SignUpDto;
import com.accenture.user.validation.annotation.ValidSignUpDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SignUpDtoValidator implements ConstraintValidator<ValidSignUpDto, SignUpDto> {
    private static Logger LOGGER = LoggerFactory.getLogger(SignUpDtoValidator.class);
    @Override
    public void initialize(ValidSignUpDto constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignUpDto signUpDto, ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }
}
