package com.accenture.user.security;

import com.accenture.user.dao.UserDao;
import com.accenture.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
public class FsaUserDetailService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FsaUserDetailService.class);
    private final UserDao userDao;
    private final JwtProvider jwtProvider;
    //

    @Autowired
    public FsaUserDetailService(UserDao userDao, JwtProvider jwtProvider) {
        this.userDao = userDao;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with username %s has not been found.", username)));

        return withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().toString())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public Optional<UserDetails> loadUserByJwtTokenRSA256(String jwtToken) {
        String username = jwtProvider.getUsername(jwtToken);
        String role = jwtProvider.getRoles(jwtToken).stream().findFirst().get().toString();//user.getRole().getAuthority()
        LOGGER.info("{} with {} is attempting to access resources", username, role);
        return Optional.of(
                withUsername(username)
                        .password("")
                        .authorities(role)
                        .accountExpired(false)
                        .accountLocked(false)
                        .credentialsExpired(false)
                        .disabled(false)
                        .build());
    }
}
