package com.sparta.onboarding.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
    // JwtUtil
    TOKEN_EXPIRATION(UNAUTHORIZED, "토큰이 만료되었습니다."),
    NOT_SUPPORTED_TOKEN(UNAUTHORIZED, "지원되지 않는 토큰 형식입니다."),
    INVALID_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_TOKEN_FORMAT(BAD_REQUEST, "잘못된 JWT 형식입니다."),
    FALSE_TOKEN(BAD_REQUEST, "잘못된 토큰입니다."),
    INVALID_TOKEN_SIGNATURE(BAD_REQUEST, "JWT 서명 검증 실패했습니다."),

    // User
    USER_NOT_FOUND(BAD_REQUEST, "사용자를 찾을 수 없습니다."),

    // Server
    GENERAL_ERROR(INTERNAL_SERVER_ERROR, "서버에서 예기치 못한 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
