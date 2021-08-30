package com.accenture.user.service;

import com.accenture.user.dao.UserDao;
import com.accenture.user.dto.SignUpDto;
import com.accenture.user.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AuthServiceTest {
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private SQSService sqsService;
    @InjectMocks
    private AuthService authService;

    @Test
    void loadAuthenticatedUser() throws Exception {
        User expectedUser = new User(32434L, "fakeUser", "password", "CUSTOMER", "fname", "lname",
                "flcust@gmail.com", "2028769868");
        Authentication authentication = new UsernamePasswordAuthenticationToken("fakeUser", "password");
        Mockito.when(userDao.findByUsername(Mockito.any())).thenReturn(Optional.of(expectedUser));
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        User actualUser = authService.loadAuthenticatedUser("fakeUser", "password")
                .orElseThrow(() -> new Exception());
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void register() {
        User expectedUser = new User(32434L, "fakeUser", "password", "CUSTOMER", "fname", "lname",
                "flcust@gmail.com", "2028769868");
        SignUpDto signUpDto = new SignUpDto("fakeUser", "password", "fname", "lname",
                "m", "343678456", "2000-02-07", "flcust@gmail.com", "2028769868");

        Mockito.when(userDao.save(Mockito.any())).thenReturn(expectedUser);
        User actualUser = authService.register(signUpDto);
        assertEquals(expectedUser, actualUser);
    }
}