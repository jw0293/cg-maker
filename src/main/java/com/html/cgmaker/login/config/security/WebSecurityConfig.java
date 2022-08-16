package com.html.cgmaker.login.config.security;

import com.html.cgmaker.login.form.handler.CustomFormLoginSuccessHandler;
import com.html.cgmaker.login.form.security.CustomAuthenticationFilter;
import com.html.cgmaker.login.form.security.CustomAuthenticationProvider;
import com.html.cgmaker.login.oauth.service.CustomOAuth2UserService;
import com.html.cgmaker.login.oauth.handler.CustomAuthenticationFailureHandler;
import com.html.cgmaker.login.oauth.handler.CustomAuthenticationSuccessHandler;
import com.html.cgmaker.login.oauth.web.repository.UserRepository;
import com.html.cgmaker.login.utils.TokenUtils;
import com.html.cgmaker.login.form.web.service.MemberDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest.StaticResourceRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final TokenUtils tokenUtils;
    private final UserDetailsService userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        StaticResourceRequestMatcher staticResourceRequestMatcher = PathRequest.toStaticResources().atCommonLocations();
        return (web) -> web.ignoring().requestMatchers(staticResourceRequestMatcher);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        http
//                        .httpBasic().disable()
//                        .csrf().disable()
//                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
//                        .authorizeRequests()
//                        .antMatchers("/token/**").permitAll()
//                        .antMatchers("/user/**").hasRole("USER")
//                        .antMatchers("/admin/**").hasRole("ADMIN")
//                        .anyRequest().permitAll()
//                    .and()
//                        .oauth2Login().loginPage("/token/expired")
//                        .successHandler(customAuthenticationSuccessHandler)
//                        .failureHandler(customAuthenticationFailureHandler)
//                        .userInfoEndpoint().userService(customOAuth2UserService)
//         http.addFilterBefore(new JwtAuthFilter(tokenUtils, userRepository), UsernamePasswordAuthenticationFilter.class);

        http
                        .csrf().disable().authorizeRequests()
                        .anyRequest().permitAll()
                .and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                        .formLogin()
                            .disable()
                            .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception{
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/user/login");
        customAuthenticationFilter.setAuthenticationSuccessHandler(customFormLoginSuccessHandler());
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    @Bean
    public CustomFormLoginSuccessHandler customFormLoginSuccessHandler(){
        return new CustomFormLoginSuccessHandler(tokenUtils);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(){
        return new CustomAuthenticationProvider((MemberDetailsServiceImpl) userDetailsService, bCryptPasswordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
