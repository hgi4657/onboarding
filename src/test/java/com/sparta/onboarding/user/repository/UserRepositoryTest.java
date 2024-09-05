package com.sparta.onboarding.user.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sparta.onboarding.user.entity.Role;
import com.sparta.onboarding.user.entity.User;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User("testuser","Passw!12","nick", Role.ROLE_USER);
        userRepository.save(user);
    }

    @Test
    @DisplayName("userRepository 저장 확인")
    void test1() {
        // given
        User testUser = new User("testUser1","Passw!12","nick", Role.ROLE_USER);

        // when
        User saveUser = userRepository.save(testUser);

        // then
        Assertions.assertThat(testUser).isEqualTo(saveUser);
        Assertions.assertThat(testUser.getUsername()).isEqualTo(saveUser.getUsername());
        Assertions.assertThat(saveUser.getId()).isNotNull();
    }

    @Test
    @DisplayName("findByUsername 메서드 확인")
    void test2() {
        // when
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }
}