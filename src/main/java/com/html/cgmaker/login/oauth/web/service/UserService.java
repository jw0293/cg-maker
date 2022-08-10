package com.html.cgmaker.login.oauth.web.service;

import com.html.cgmaker.login.domain.constants.AuthConstants;
import com.html.cgmaker.login.oauth.web.entity.User;
import com.html.cgmaker.login.domain.enums.UserRole;
import com.html.cgmaker.login.utils.TokenUtils;
import com.html.cgmaker.login.oauth.dto.OAuthAttributes;
import com.html.cgmaker.login.oauth.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final TokenUtils tokenUtils;

    public Optional<User> findByEmail(final String email){
        return userRepository.findByUserEmail(email);
    }

    public void returnUser(HttpServletResponse response){
        String token = response.getHeader(AuthConstants.AUTH_HEADER);
        String email = tokenUtils.getUid(token);

        log.info("returnUserEmail = {}", email);
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
