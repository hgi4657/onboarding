package com.sparta.onboarding.common.security;

import com.sparta.onboarding.common.exception.CustomException;
import com.sparta.onboarding.common.exception.ErrorEnum;
import com.sparta.onboarding.user.entity.User;
import com.sparta.onboarding.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(ErrorEnum.USER_NOT_FOUND));
        return new UserDetailsImpl(user);
    }
}
