package com.accenture.user.security;

import com.accenture.user.entity.User;
import com.accenture.user.entity.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtProvider {
    private final String ROLES_KEY = "roles";
    private final long validityInMilliseconds;
    private final KeyPair keyPair;

    public JwtProvider(@Value("${security.jwt.token.expiration}") long validityInMilliseconds,
                       @Value("${AUTH_JWT_PRIVATE_KEY}") final String privateKey,
                       @Value("${AUTH_JWT_PUBLIC_KEY}") final String publicKey) {
        this.validityInMilliseconds = validityInMilliseconds;
        this.keyPair = generateKeyPair(publicKey, privateKey);
    }

    private KeyPair generateKeyPair(String publicKey, String privateKey) {
        try {
            return new KeyPair(generatePublicKey(publicKey), generatePrivateKey(privateKey));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }

    private PrivateKey generatePrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        privateKey = parseKey(privateKey);
        final byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKey);
        final KeySpec keySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey generatePublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicKey = parseKey(publicKey);
        final byte[] decodedPublicKey = Base64.getDecoder().decode(publicKey);
        final KeySpec keySpec = new X509EncodedKeySpec(decodedPublicKey);
        return keyFactory.generatePublic(keySpec);
    }

    private String parseKey(String key) {
        return key.replaceAll("-----BEGIN (.*?)-----", "").replaceAll("-----END (.*)----", "")
                .replaceAll("\r\n", "").replaceAll("\\\\n", "").replaceAll("\n", "").trim();
    }

    public String createToken(String username, List<UserRole> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(ROLES_KEY, roles.stream().map(role ->
                new SimpleGrantedAuthority(role.getAuthority()))
                .filter(Objects::nonNull).collect(Collectors.toList()));
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(keyPair.getPublic()).parseClaimsJws(token).getBody().getSubject();
    }

    public List<GrantedAuthority> getRoles(String token) {
        List<Map<String, String>> roleClaims = Jwts.parser()
                .setSigningKey(keyPair.getPublic()).parseClaimsJws(token).getBody()
                .get(ROLES_KEY, List.class);
        return roleClaims.stream().map(roleClaim -> new SimpleGrantedAuthority(
                roleClaim.get("authority"))).collect(Collectors.toList());
    }

    public Optional<String> parseTokenRS256(String token) {
        try {
            final String decoded = decode(token);
            return Optional.of(decoded);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String decode(final String token) {
        return new String(Base64.getDecoder().decode(token));
    }

    private String encode(final String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public boolean isValidTokenRS256(String token) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(keyPair.getPublic()).parseClaimsJws(token).getBody();
            if (claims.getExpiration().before(new Date()))
                return false;
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Optional<Cookie> createSessionCookie(User user) {
        try {
            List<UserRole> roles = new ArrayList<>();
            roles.add(new UserRole(user.getRole().name()));
            final String token = createToken(user.getUsername(), roles);
//            final ObjectMapper objectMapper = new ObjectMapper();
//            final Cookie sessionCookie = new Cookie("session", encode(objectMapper.writeValueAsString(to)));
            final Cookie sessionCookie = new Cookie("session", encode(token));
            sessionCookie.setPath("/");
            // sessionCookie.setHttpOnly(true);
            return Optional.of(sessionCookie);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
