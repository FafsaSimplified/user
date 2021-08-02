package com.accenture.user.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);
    private final FsaUserDetailService fsaUserDetailService;
    private final JwtProvider jwtProvider;

    @Autowired
    public JwtTokenFilter(FsaUserDetailService fsaUserDetailService, JwtProvider jwtProvider) {
        this.fsaUserDetailService = fsaUserDetailService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("Process request to check for a JSON Web Token");
        final Cookie[] cookies = request.getCookies();
//    && cookie.isHttpOnly()
        try {
            final Optional<Cookie> sessionCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().matches("^session$")).findFirst();
            if (sessionCookie.isPresent()) {
                String parsedToken = jwtProvider.parseTokenRS256(sessionCookie.get().getValue())
                        .orElseThrow(() -> new Exception());
                if (jwtProvider.isValidTokenRS256(parsedToken)) {
                    Optional<UserDetails> userDetails = fsaUserDetailService.loadUserByJwtTokenRSA256(parsedToken);
                    userDetails.ifPresent(userDetail ->
                            SecurityContextHolder.getContext().setAuthentication(
                                    new PreAuthenticatedAuthenticationToken(
                                            userDetail,
                                            "",
                                            userDetail.getAuthorities())));
                } else {
                    LOGGER.error("Token is not valid");
                }
            }
        } catch (Exception e) {
            LOGGER.error("Not able to identify the user");
        }
        filterChain.doFilter(request, response);
    }
}
