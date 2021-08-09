package com.accenture.user.service;

import com.accenture.user.dao.UserDao;
import com.accenture.user.dto.SignUpDto;
import com.accenture.user.entity.User;
import com.accenture.user.entity.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SQSService sqsService;

    @Autowired
    public AuthService(UserDao userDao, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, SQSService sqsService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.sqsService = sqsService;
    }

    public Optional<User> loadAuthenticatedUser(final String id, final String password) {
        LOGGER.info("New user is attempting to log in.");
        String username = null;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(id, password));

            LOGGER.info("{} has successfully logged in", authentication.getPrincipal());
            if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
                org.springframework.security.core.userdetails.User user =
                        (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
                LOGGER.info("{} has successfully logged in", user.getUsername());
                username = user.getUsername();
            }
        } catch (AuthenticationException e) {
            LOGGER.error("Log in authentication failed for user {}", id);
            LOGGER.error("{}", e.getMessage());
            return Optional.empty();
        }
        return userDao.findByUsername(username);
    }

    public User register(SignUpDto signUpDto) {
        LOGGER.info("New user attempting to sign up");
        User user = new User(signUpDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(new UserRole("CUSTOMER"));
//        String now = ZonedDateTime.now(ZoneOffset.UTC)
//                .format(DateTimeFormatter.ofPattern("uuuu.MM.dd.HH.mm.ss"));
        String now = ZonedDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_DATE_TIME);
        // java 8 bug
        user.setCreateTime(now.substring(0, now.length() - 1));
        User addedUser = null;
        try {
            // uncomment actual code
            addedUser = userDao.save(user);
            this.sqsService.pushToSQS(addedUser.getEmail(), addedUser.getFirstName());
            // this.sqsService.pushToSQS(signUpDto.getEmail(), signUpDto.getFirstName());

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        return addedUser;
    }
}
