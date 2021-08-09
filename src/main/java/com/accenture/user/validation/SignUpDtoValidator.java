package com.accenture.user.validation;

import com.accenture.user.dto.SignUpDto;
import com.accenture.user.validation.annotation.ValidSignUpDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SignUpDtoValidator implements ConstraintValidator<ValidSignUpDto, SignUpDto> {
    private static Logger LOGGER = LoggerFactory.getLogger(SignUpDtoValidator.class);

    @Override
    public void initialize(ValidSignUpDto constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignUpDto signUpDto, ConstraintValidatorContext constraintValidatorContext) {
//        assertPasswordValid(signUpDto);
        return true;
    }

    private boolean assertPasswordValid(SignUpDto signUpDto) {
        if (signUpDto.getPassword() == null) {
            LOGGER.error("password must contain at least 8 characters");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password must be provided");
        }
        String password = signUpDto.getPassword();
        if (!password.matches("^.*[a-z].*$")) {
            LOGGER.error("password must contain at least 8 characters");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password must contain at least one lowercase letter");
        }
        if (!password.matches("^.*[A-Z].*$")) {
            LOGGER.error("password must contain at least 8 characters");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password must contain at least one uppercase letter");
        }
        if (!password.matches("^.*\\d.*$")) {
            LOGGER.error("password must contain at least 8 characters");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password must contain at least one digit");
        }
        if (!password.matches("^[a-zA-Z\\d@$!%*#?&]{8,30}$")) {
            LOGGER.error("password must contain at least 8 characters");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password must contain at least 8 characters");
        }
        return true;
    }

    public void assertDobValid(SignUpDto signUpDto) {
        if (signUpDto.getDob() == null) {
            LOGGER.error("Dob must be provided");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dob must be provided");
        }
        try {
            LocalDate dob = LocalDate.parse(signUpDto.getDob());
            // invalid year; cannot pass a year greater than current year
            if (LocalDate.now().getYear() < dob.getYear()) {
                LOGGER.error("Invalid year");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid year");
            }
            // age cannot be less than 15 years
            if ((LocalDate.now().getYear() - dob.getYear()) < 15) {
                LOGGER.error("Invalid year");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid year");
            }
        } catch (DateTimeParseException e) {
            LOGGER.error("Dob is not formatted correctly");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dob is not formatted correctly");
        }
    }

    public void assertSsnValid(SignUpDto signUpDto) {
        if (signUpDto.getSsn() == null) {
            LOGGER.error("ssn must be provided");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ssn must be provided");
        }
        String ssn = signUpDto.getSsn();
        if (!ssn.matches("(\\d){9}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ssn is not in correct format");
        }
    }

    public void assertEmailValid(SignUpDto signUpDto) {
        if (signUpDto.getEmail() == null) {
            LOGGER.error("email must be provided");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ssn must be provided");
        }
        String email = signUpDto.getSsn();
        if (!email.matches(".+@.+..+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email is not in correct format");
        }
    }

    public void assertUsernameValid(SignUpDto signUpDto) {
        if (signUpDto.getUsername() == null) {
            LOGGER.error("username must be provided");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username must be provided");
        }
        String username = signUpDto.getSsn();
        if (!username.matches("[a-zA-Z\\d]{6,30}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username can only contain lowercase," +
                    " uppercase, and digits");
        }
        if (username.matches("[\\d]{10}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username cannot have 10 numbers and no letters");
        }
    }

    public void assertUserDataValid(SignUpDto signUpDto) {
        assertUsernameValid(signUpDto);
        assertSsnValid(signUpDto);
        assertDobValid(signUpDto);
        assertEmailValid(signUpDto);
    }
}
