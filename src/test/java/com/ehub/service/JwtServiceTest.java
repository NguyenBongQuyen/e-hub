package com.ehub.service;

import com.ehub.common.TokenType;
import com.ehub.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {
    @InjectMocks
    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "expiryMinutes", 15L);
        ReflectionTestUtils.setField(jwtService, "expiryDay", 7L);
        ReflectionTestUtils.setField(jwtService, "accessKey", Base64.getEncoder().encodeToString("test_access_key_1234567890123456".getBytes()));
        ReflectionTestUtils.setField(jwtService, "refreshKey", Base64.getEncoder().encodeToString("test_refresh_key_1234567890123456".getBytes()));
    }

    @Test
    void testGenerateAccessToken() {
        String username = "newbie";
        List<String> authorities = List.of("USER", "ADMIN");

        String token = jwtService.generateAccessToken(username, authorities);
        assertNotNull(token);

        // Giải mã token để kiểm tra thông tin bên trong
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(Objects.requireNonNull(ReflectionTestUtils.getField(jwtService, "accessKey")).toString())))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, claims.getSubject()); // Kiểm tra username
        assertEquals(authorities, claims.get("role", List.class)); // Kiểm tra role
    }

    @Test
    void testGenerateRefreshToken() {
        String username = "newbie";
        List<String> authorities = List.of("USER", "ADMIN");

        String token = jwtService.generateRefreshToken(username, authorities);
        assertNotNull(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(Objects.requireNonNull(ReflectionTestUtils.getField(jwtService, "refreshKey")).toString())))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, claims.getSubject());
        assertEquals(authorities, claims.get("role", List.class));
    }

    @Test
    void testExtractUsername() {
        String username = "newbie";
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(Objects.requireNonNull(ReflectionTestUtils.getField(jwtService, "accessKey")).toString()));

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String extractedUsername = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);

        assertEquals(username, extractedUsername);
    }
}
