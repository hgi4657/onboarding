package com.sparta.onboarding.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignResponse {
    private String token;

    @Builder
    public SignResponse(String token) {
        this.token = token;
    }
}
