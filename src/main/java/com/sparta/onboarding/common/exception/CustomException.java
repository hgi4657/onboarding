package com.sparta.onboarding.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public CustomException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.status = errorEnum.getHttpStatus();
        this.message = errorEnum.getMessage();
    }
}
