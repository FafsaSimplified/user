package com.accenture.user.validation;

import com.accenture.user.dto.LoginDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return LoginDto.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        LoginDto loginDto = (LoginDto) o;
        if (loginDto.getUsername() == null) {
            errors.rejectValue("username", "username cannot be null");
        }
    }
}
