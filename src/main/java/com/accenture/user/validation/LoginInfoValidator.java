package com.accenture.user.validation;

import com.accenture.user.dto.LoginDto;
import com.accenture.user.validation.annotation.ValidLoginDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginInfoValidator implements ConstraintValidator<ValidLoginDto, LoginDto> {
    private static Logger LOGGER = LoggerFactory.getLogger(LoginInfoValidator.class);

    @Override
    public void initialize(ValidLoginDto constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LoginDto loginDto, ConstraintValidatorContext constraintValidatorContext) {
        if (loginDto.getUsername() == null && loginDto.getEmail() == null && loginDto.getPhone() == null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Either one of username, email or phone number must be provided")
                    .addConstraintViolation();
            LOGGER.error("Either one of username, email or phone number must be provided");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either one of username, email or phone number must be provided");
//            return false;
        }
        if (loginDto.getPassword() == null) {
            LOGGER.error("Password is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password should be provided");
        }
        return true;
    }
}
