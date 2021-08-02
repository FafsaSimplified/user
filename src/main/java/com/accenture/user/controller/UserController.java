package com.accenture.user.controller;

import com.accenture.user.dto.LoginDto;
import com.accenture.user.dto.SignUpDto;
import com.accenture.user.entity.User;
import com.accenture.user.security.JwtProvider;
import com.accenture.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public User signUp(@RequestBody @Valid SignUpDto signUpDto) {
//        return userService.signUp(signUpDto);
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User signUp(@RequestBody @Valid User user) {
        return userService.signUp(user);
    }

    @GetMapping
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0", required = false) int page,
                                  @RequestParam(defaultValue = "20", required = false) int limit,
                                  @RequestParam(required = false) Map<String, String> filters) {
        return userService.getAllUsers(page, limit, filters);
    }

    @PostMapping("/session")
    @ResponseStatus(HttpStatus.CREATED)
    public User login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        final User user = userService.loadAuthenticatedUser(loginDto.getUsername(), loginDto.getPassword())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "Login Failed"));
        final Cookie sessionCookie = jwtProvider.createSessionCookie(user)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "token creation has failed."));
        response.addCookie(sessionCookie);
        final User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setFirstName("testFirstname");
        return user.generalAccess();
    }
}


// Handling validator using initBinder
//    private final LoginDtoValidator loginDtoValidator;
//
//    @Autowired
//    public UserController(LoginDtoValidator loginDtoValidator) {
//        this.loginDtoValidator = loginDtoValidator;
//    }
//
//    @InitBinder()
//    public void initMerchantOnlyBinder(WebDataBinder binder) {
//        binder.addValidators(loginDtoValidator);
//    }