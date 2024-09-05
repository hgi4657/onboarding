package com.sparta.onboarding.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignRequest {
    @NotBlank(message = "필수 항목입니다.")
    @Size(max = 30, message = "최대 30자까지 입력 가능합니다.")
    private String username;

    @NotBlank(message = "필수 항목입니다.")
    @Size(min = 6, max = 50, message = "최소 6자 이상, 최대 50자 이하까지 입력 가능합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}", message = "영문 대문자, 숫자, 특수문자가 1개 이상 포함 되어야 합니다.")
    private String password;

    @Builder
    public SignRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
