package com.accenture.user.service;

import com.accenture.user.UserApplication;
import com.accenture.user.dao.UserDao;
import com.accenture.user.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @Test
    void signUp() {
        User expectedUser = new User(32434L, "fakeUser", "password", "ADMIN", "fname", "lname",
                "flcust@gmail.com", "2028769868");
        Mockito.when(userDao.save(Mockito.any())).thenReturn(expectedUser);
        User actualUser = userService.signUp(expectedUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void loadAuthenticatedUser() throws Exception {
        User expectedUser = new User(32434L, "fakeUser", "password", "ADMIN", "fname", "lname",
                "flcust@gmail.com", "2028769868");
        Mockito.when(userDao.findByUsername(Mockito.any())).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.loadAuthenticatedUser("fakeUser", "password").orElseThrow(Exception::new);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        User fakeUser1 = new User(32434L, "fakeUser1", "password", "ADMIN", "fname", "lname",
                "flcust@gmail.com", "2028769868");
        User fakeUser2 = new User(34344324L, "fakeUser2", "password", "ADMIN", "fname", "lname",
                "flcust@gmail.com", "2028769868");
        User fakeUser3 = new User(4534L, "fakeUser3", "password", "ADMIN", "fname", "lname",
                "flcust@gmail.com", "2028769868");
        User fakeUser4 = new User(65434L, "fakeUser4", "password", "ADMIN", "fname", "lname",
                "flcust@gmail.com", "2028769868");
        users.add(fakeUser1);
        users.add(fakeUser2);
        users.add(fakeUser3);
        users.add(fakeUser4);
        Page<User> expectedUsers = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(userDao.findAll(pageable)).thenReturn(expectedUsers);
        assertEquals(expectedUsers, userService.getAllUsers(0, 20, null));
    }
}