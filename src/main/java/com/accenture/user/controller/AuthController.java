package com.accenture.user.controller;

import com.accenture.user.dto.LoginDto;
import com.accenture.user.dto.SignUpDto;
import com.accenture.user.entity.User;
import com.accenture.user.security.JwtProvider;
import com.accenture.user.service.AuthService;
import com.accenture.user.validation.SignUpDtoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final SignUpDtoValidator signUpDtoValidator = new SignUpDtoValidator();

    @Autowired
    public AuthController(AuthService authService, JwtProvider jwtProvider) {
        this.authService = authService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * endpoint that is used to log in a user to their account. This will create a session cookie for the user
     *
     * @param loginDto {id, password} where id can be a username, email, or phone
     * @param response
     * @return user information
     */
    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.CREATED)
    public User authenticate(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        final User user = authService.loadAuthenticatedUser(loginDto.getId(), loginDto.getPassword())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login Failed"));
        final Cookie sessionCookie = jwtProvider.createSessionCookie(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "token creation has failed."));
        response.addCookie(sessionCookie);
        return user.generalAccess();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response){
        final Cookie sessionCookie = new Cookie("session", null);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
    }

    @PostMapping("/registration/checkDob")
    public boolean checkDob(@RequestBody SignUpDto signUpDto) {
        signUpDtoValidator.assertDobValid(signUpDto);
        return true;
    }

    @PostMapping("/registration/checkSsn")
    public boolean checkSsn(@RequestBody SignUpDto signUpDto) {
        signUpDtoValidator.assertSsnValid(signUpDto);
        return true;
    }

    @PostMapping("/registration/checkEmail")
    public boolean checkEmail(@RequestBody SignUpDto signUpDto) {
        signUpDtoValidator.assertEmailValid(signUpDto);
        return true;
    }

    @PostMapping("/registration/checkUsername")
    public boolean checkUsername(@RequestBody SignUpDto signUpDto) {
        signUpDtoValidator.assertUsernameValid(signUpDto);
        return true;
    }

    @PostMapping("/registration/checkUserData")
    public boolean checkUserData(@RequestBody SignUpDto signUpDto) {
        signUpDtoValidator.assertUserDataValid(signUpDto);
        return true;
    }

    @PostMapping("/registration/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerANewUser(@RequestBody @Valid SignUpDto signUpDto) {
//        signUpDtoValidator.assertUserDataValid(signUpDto);
        return authService.register(signUpDto);
    }

}
