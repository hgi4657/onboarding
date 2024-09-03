package com.sparta.onboarding.user.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupResponse {
    private String username;
    private String nickname;
    private List<AuthorityResponse> authorities;

    @Builder
    public SignupResponse(String username, String nickname, List<AuthorityResponse> authorities) {
        this.username = username;
        this.nickname = nickname;
        this.authorities = authorities;
    }
}