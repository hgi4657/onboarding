package com.sparta.onboarding.user.service;

import com.sparta.onboarding.common.exception.CustomException;
import com.sparta.onboarding.common.exception.ErrorEnum;
import com.sparta.onboarding.user.dto.AuthorityResponse;
import com.sparta.onboarding.user.dto.SignupRequest;
import com.sparta.onboarding.user.dto.SignupResponse;
import com.sparta.onboarding.user.entity.Role;
import com.sparta.onboarding.user.entity.User;
import com.sparta.onboarding.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        String username = request.getUsername();
        String password = passwordEncoder.encode(request.getPassword());

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new CustomException(ErrorEnum.DUPLICATE_USER);
        }

        User user = new User(username, password, request.getNickname(), Role.ROLE_USER);
        userRepository.save(user);

        List<AuthorityResponse> authorities = user.getRoles().stream()
            .map(role -> AuthorityResponse.builder()
                .authorityName(role.getAuthority())
                .build())
            .toList();

        return SignupResponse.builder()
            .username(user.getUsername())
            .nickname(user.getNickname())
            .authorities(authorities)
            .build();
    }
}
