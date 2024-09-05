package com.sparta.onboarding.user.controller;

import com.sparta.onboarding.user.dto.SignRequest;
import com.sparta.onboarding.user.dto.SignResponse;
import com.sparta.onboarding.user.dto.SignupRequest;
import com.sparta.onboarding.user.dto.SignupResponse;
import com.sparta.onboarding.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     *
     * @param request 유저 정보
     * @return 회원가입 응답 데이터
     */
    @PostMapping("/signup")
    public SignupResponse signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = userService.signup(request);
        return response;
    }

    /**
     * 로그인
     *
     * @param request 로그인 정보
     * @return 발급된 Access 토큰
     */
    @PostMapping("/sign")
    public SignResponse sign(@Valid @RequestBody SignRequest request) {
        SignResponse response = userService.sign(request);
        return response;
    }

}
