package com.accenture.user.service;

import com.accenture.user.dao.UserDao;
import com.accenture.user.dto.SignUpDto;
import com.accenture.user.entity.User;
import com.accenture.user.entity.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    //    public User signUp(SignUpDto signUpDto) {
//        LOGGER.info("New user attempting to sign up");
//        User newUser = new User();
//        return newUser;
//    }
    public User signUp(User user) {
        LOGGER.info("New user attempting to sign up");
        user.setPassword("password");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(new UserRole("CUSTOMER"));
        String now = ZonedDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("uuuu.MM.dd.HH.mm.ss"));
        user.setCreateTime(now);
        User addedUser = userDao.save(user);
        return addedUser;
    }

    public Optional<User> loadAuthenticatedUser(final String username, final String password) {
        LOGGER.info("New user is attempting to sign in.");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            LOGGER.error("Log in authentication failed for user {}", username);
            return Optional.empty();
        }
        return userDao.findByUsername(username);
    }

    @Transactional
    public Page<User> getAllUsers(int page, int limit, Map<String, String> filters) {
        Pageable pageable = PageRequest.of(page, limit);
        return userDao.findAll(pageable);
    }
}
