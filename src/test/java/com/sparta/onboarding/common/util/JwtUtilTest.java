package com.sparta.onboarding.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.onboarding.common.exception.CustomException;
import com.sparta.onboarding.common.exception.ErrorEnum;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private String secretKey = "pB+EU94ugGpGZeRZh7pPi+aW1Jcv3uE9Zs1y2rBOw3IFJY/1ttMyz3K438sUyzQQdNAluzR3a9x+M0ZRyeuNhw==";
    private Key key;
    private String token;

    @BeforeEach
    public void setUp() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtUtil.setSECRET_KEY(secretKey);
        jwtUtil.init();

        token = Jwts.builder()
            .setSubject("testUser")
            .claim("auth", "USER")
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour validity
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    @Test
    @DisplayName("validateToken - 정상 토큰")
    void verifySignature() {
        // given
        // when & then
        jwtUtil.verifySignature(token);
    }

    @Test
    @DisplayName("validateToken - 만료된 토큰")
    void validateToken1() {
        // given
        String expiredToken = Jwts.builder()
            .setSubject("testUser")
            .claim("auth", "USER")
            .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 10)) // 10분 전
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            jwtUtil.validateToken(expiredToken);
        });

        // Then
        assertThat(exception.getMessage()).isEqualTo(ErrorEnum.TOKEN_EXPIRATION.getMessage());
    }

    @Test
    @DisplayName("validateToken - 잘못된 토큰")
    void validateToken2() {
        // given
        String invalidToken = token.substring(0, token.length() - 1) + "x";

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            jwtUtil.validateToken(invalidToken);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo(ErrorEnum.INVALID_TOKEN.getMessage());
    }

    @Test
    @DisplayName("validateToken - 유효하지 않은 토큰")
    void validateToken3() {
        // given
        String unsupportedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0VXNlcjEiLCJleHBpcmF0aW9uIjoiMTYwNTY1NDExNSJ9.CvS4_pD1PxqEyJPiU_Xt-4z5p8I5HpMyxk_9h8-pnM0";

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            jwtUtil.validateToken(unsupportedToken);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo(ErrorEnum.INVALID_TOKEN.getMessage());
    }

    @Test
    @DisplayName("validateToken - 잘못된 토큰")
    void validateToken4() {
        // given
        String unsupportedToken = "UnsupportedTokenFormat";

        // then
        CustomException exception = assertThrows(CustomException.class, () -> {
            jwtUtil.validateToken(unsupportedToken);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo(ErrorEnum.INVALID_TOKEN_FORMAT.getMessage());
    }

    @Test
    @DisplayName("validateToken - 잘못된 형식의 토큰")
    void validateToken_FalseToken() {
        // Given
        String falseToken = "";

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            jwtUtil.validateToken(falseToken);
        });

        // Then
        assertThat(exception.getMessage()).isEqualTo(ErrorEnum.FALSE_TOKEN.getMessage());
    }
}