package com.html.cgmaker.signup.config.filter;

import com.html.cgmaker.signup.domain.dto.UserDto;
import com.html.cgmaker.signup.domain.repository.UserRepository;
import com.html.cgmaker.signup.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Log4j2
public class JwtAuthFilter extends GenericFilterBean {

    private final TokenUtils tokenUtils;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        String filterToken = ((HttpServletRequest) request).getHeader("Auth");

        log.info("JwtAuthFilter Token = {}", filterToken);

        if(filterToken != null && tokenUtils.isValidToken(filterToken)){

            String email = tokenUtils.getUid(filterToken);

            UserDto userDto = userRepository.findByUserEmail(email)
                    .map(u -> new UserDto(u.getUserEmail(), u.getUserName(), u.getPicturePath()))
                    .orElseThrow(() -> new UsernameNotFoundException(email));

            Authentication auth = getAuthentication(userDto);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(UserDto userDto){
        return new UsernamePasswordAuthenticationToken(userDto, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
