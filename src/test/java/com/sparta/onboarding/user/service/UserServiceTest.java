package com.sparta.onboarding.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.onboarding.common.exception.CustomException;
import com.sparta.onboarding.common.exception.ErrorEnum;
import com.sparta.onboarding.common.util.JwtUtil;
import com.sparta.onboarding.user.dto.SignRequest;
import com.sparta.onboarding.user.dto.SignResponse;
import com.sparta.onboarding.user.dto.SignupRequest;
import com.sparta.onboarding.user.dto.SignupResponse;
import com.sparta.onboarding.user.entity.Role;
import com.sparta.onboarding.user.entity.User;
import com.sparta.onboarding.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 정상 테스트")
    void test1() {
        // given
        SignupRequest request = SignupRequest.builder()
            .username("testUser")
            .nickname("test")
            .password("Pass!12")
            .build();

        // when
        SignupResponse response = userService.signup(request);

        // then
        assertNotNull(response);
        assertEquals("testUser", response.getUsername());
        assertEquals("test", response.getNickname());
        verify(passwordEncoder).encode(request.getPassword());
    }

    @Test
    @DisplayName("회원가입 실패 텍스트 - 중복")
    void test2() {
        // given
        SignupRequest request = SignupRequest.builder()
            .username("testuser")
            .password("testpassword")
            .nickname("Test User")
            .build();

        User existingUser = new User("testuser", "encodedpassword", "Test User", Role.ROLE_USER);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(existingUser));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> userService.signup(request));
        assertEquals(ErrorEnum.DUPLICATE_USER.getMessage(), exception.getMessage());
        verify(userRepository).findByUsername(request.getUsername());
    }

    @Test
    @DisplayName("로그인 정상 테스트")
    void test3() {
        // given
        SignRequest request = SignRequest.builder()
            .username("testuser")
            .password("encodedpassword")
            .build();

        User user = new User("testuser", "encodedpassword", "Test", Role.ROLE_USER);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.createAccessToken(user.getUsername(), user.getRole())).thenReturn("accessToken");
        when(jwtUtil.createRefreshToken(user.getUsername(), user.getRole())).thenReturn("refreshToken");

        // when
        SignResponse response = userService.sign(request);

        // then
        assertEquals("accessToken", response.getToken());
        verify(userRepository).findByUsername(request.getUsername());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 오류")
    void test4() {
        // given
        SignRequest request = SignRequest.builder()
            .username("testuser")
            .password("wrongpassword")
            .build();

        User user = new User("testuser", "encodedpassword", "Test User", Role.ROLE_USER);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> userService.sign(request));
        assertEquals(ErrorEnum.INCORRECT_PASSWORD.getMessage(), exception.getMessage());
        verify(userRepository).findByUsername(request.getUsername());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
    }
}