package com.html.cgmaker.signup.domain.service;

import com.html.cgmaker.signup.config.auth.dto.OAuthAttributes;
import com.html.cgmaker.signup.domain.user.User;
import com.html.cgmaker.signup.domain.repository.UserRepository;
import com.html.cgmaker.signup.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByEmail(final String email){
        return userRepository.findByUserEmail(email);
    }

    @Transactional
    public User saveOrUpdate(OAuthAttributes attributes){
        User user = userRepository.findByUserEmail(attributes.getUserEmail())
                .map(entity -> entity.update(attributes.getUserName(), attributes.getPicturePath()))
                .orElse(attributes.toEntity());

        User saveUser = User.builder()
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .role(UserRole.USER)
                .picturePath(user.getPicturePath())
                .build();

        return userRepository.save(saveUser);
    }
}
