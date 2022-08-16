package com.html.cgmaker.login.config.filter;

import com.html.cgmaker.login.domain.constants.AuthConstants;
import com.html.cgmaker.login.domain.enums.UserRole;
import com.html.cgmaker.login.oauth.web.dto.UserDto;
import com.html.cgmaker.login.oauth.web.repository.UserRepository;
import com.html.cgmaker.login.utils.TokenUtils;
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

        String filterToken = ((HttpServletRequest) request).getHeader(AuthConstants.AUTH_HEADER);

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
                Arrays.asList(new SimpleGrantedAuthority(UserRole.USER.getKey())));
    }
}
