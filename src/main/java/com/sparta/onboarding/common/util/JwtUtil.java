package com.sparta.onboarding.common.util;

import com.sparta.onboarding.common.exception.CustomException;
import com.sparta.onboarding.common.exception.ErrorEnum;
import com.sparta.onboarding.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    // KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_KEY = "auth";

    @Setter
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;
    @Value("${jwt.time.access}")
    private Long ACCESS_TOKEN_TIME;
    @Value("${jwt.time.refresh}")
    private Long REFRESH_TOKEN_TIME;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(SECRET_KEY);
        key = Keys.hmacShaKeyFor(bytes);
    }

    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public boolean validateToken(String token) {
        try {
            verifySignature(token);

            Claims claims = getUserInfoFromToken(token);
            if (claims.getExpiration().before(new Date())) {
                throw new CustomException(ErrorEnum.TOKEN_EXPIRATION);
            }
            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorEnum.TOKEN_EXPIRATION);
        } catch (MalformedJwtException e) {
            throw new CustomException(ErrorEnum.INVALID_TOKEN_FORMAT);
        } catch (SecurityException e) {
            throw new CustomException(ErrorEnum.INVALID_TOKEN_SIGNATURE);
        } catch (JwtException e) {
            throw new CustomException(ErrorEnum.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorEnum.FALSE_TOKEN);
        }
    }

    public String createAccessToken(String username, Role role) {
        return createToken(username, role, ACCESS_TOKEN_TIME);
    }

    public String createRefreshToken(String username, Role role) {
        return createToken(username, role, REFRESH_TOKEN_TIME);
    }

    public String substringToken(String token) {
        return token.substring(BEARER_PREFIX.length());
    }

    public String getTokenFromRequest(HttpServletRequest req) {
        return req.getHeader(AUTHORIZATION_HEADER);
    }

    public String getRefreshTokenFromRequest(HttpServletRequest req) {
        return req.getHeader(REFRESH_TOKEN_HEADER);
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public void verifySignature(String token) {
        Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token);
    }

    private String createToken(String username, Role role, Long tokenTime) {
        Date date = new Date();
        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role.getAuthority())
                .setExpiration(new Date(date.getTime() + tokenTime))
                .setIssuedAt(date)
                .signWith(getSigningKey(), signatureAlgorithm)
                .compact();
    }
}
