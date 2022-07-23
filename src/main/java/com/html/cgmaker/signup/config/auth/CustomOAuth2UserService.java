package com.html.cgmaker.signup.config.auth;

import com.html.cgmaker.signup.config.auth.dto.OAuthAttributes;
import com.html.cgmaker.signup.domain.user.User;
import com.html.cgmaker.signup.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails.UserInfoEndpoint;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // userRequest가 NULL 값을 나타낼 수 없다는 것을 의미
        Assert.notNull(userRequest, "userRequest cannot be null");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        // UserInfoEndpoint를 이용하여 사용자에 대한 ID 검색이 가능
        UserInfoEndpoint userInfoEndpoint = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint();

        // 현재 로그인 진행 중인 서비스를 구분하는 코드, 구글 or Github
        String registrationId = userRequest.getClientRegistration().getRegistrationId();


        // 사용자 infoEndpoint에 대한 URI 반환
        String userInfoUri = userInfoEndpoint.getUri();

        // OAuth2 로그인 진행 시 키가 되는 필드 값 (= Primary Key와 같은 의미)
        // 구글의 경우 기본적으로 코드를 지원, "sub"
        String userNameAttributeName = userInfoEndpoint.getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 유저 정보 저장 또는 업데이트
        User user = saveOrUpdate(attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    public User saveOrUpdate(OAuthAttributes attributes){
        User user = userRepository.findByUserEmail(attributes.getUserEmail())
                .map(entity -> entity.update(attributes.getUserName(), attributes.getPicturePath()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
