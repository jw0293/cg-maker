package com.html.cgmaker.login.oauth.service;

import com.html.cgmaker.login.oauth.dto.OAuthAttributes;
import com.html.cgmaker.login.oauth.web.entity.User;
import com.html.cgmaker.login.oauth.web.service.UserService;
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

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

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
        User user = userService.saveOrUpdate(attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

}
