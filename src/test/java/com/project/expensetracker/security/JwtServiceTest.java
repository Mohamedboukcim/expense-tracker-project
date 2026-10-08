package com.project.expensetracker.security;

import com.project.expensetracker.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret",
                "cle-de-test-suffisamment-longue-pour-hmac-sha256-algorithm");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3600000L);

        User user = User.builder().id(1L).email("test@test.com").fullName("Test").build();
        userPrincipal = new UserPrincipal(user);
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        String token = jwtService.generateToken(userPrincipal);

        assertThat(token).isNotBlank();
        assertThat(jwtService.extractEmail(token)).isEqualTo("test@test.com");
        assertThat(jwtService.extractUserId(token)).isEqualTo(1L);
    }

    @Test
    void isTokenValid_shouldReturnTrue_forFreshToken() {
        String token = jwtService.generateToken(userPrincipal);

        assertThat(jwtService.isTokenValid(token, "test@test.com")).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenEmailDoesNotMatch() {
        String token = jwtService.generateToken(userPrincipal);

        assertThat(jwtService.isTokenValid(token, "autre@test.com")).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenIsExpired() {
        ReflectionTestUtils.setField(jwtService, "expirationMs", -1000L);
        String expiredToken = jwtService.generateToken(userPrincipal);

        assertThat(jwtService.isTokenValid(expiredToken, "test@test.com")).isFalse();
    }
}