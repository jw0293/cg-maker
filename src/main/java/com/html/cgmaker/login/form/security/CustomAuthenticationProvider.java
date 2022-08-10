package com.html.cgmaker.login.form.security;

import com.html.cgmaker.login.form.web.entity.MyUserDetails;
import com.html.cgmaker.login.form.web.service.MemberDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final MemberDetailsServiceImpl memberDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        final String email = token.getName();
        final String password = (String) token.getCredentials();

        final MyUserDetails userDetails = (MyUserDetails) memberDetailsService.loadUserByUsername(email);

        if(!bCryptPasswordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException(userDetails.getName() + "Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
