package com.sparta.onboarding.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthorityResponse {
    private String authorityName;

    @Builder
    public AuthorityResponse(String authorityName) {
        this.authorityName = authorityName;
    }
}
