package com.html.cgmaker.login.config.web;

import com.html.cgmaker.login.form.interceptor.JwtTokenInterceptor;
import com.html.cgmaker.login.config.filter.HeaderFilter;
import com.html.cgmaker.login.utils.TokenUtils;
import com.html.cgmaker.login.form.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenUtils tokenUtils;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor())
                .excludePathPatterns("/user/signUp")
                .addPathPatterns("/user/**");
    }

    @Bean
    public FilterRegistrationBean<HeaderFilter> getFilterRegistrationBean(){
        FilterRegistrationBean<HeaderFilter> registrationBean = new FilterRegistrationBean<>(createHeaderFilter());
        registrationBean.setOrder(Integer.MIN_VALUE);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public HeaderFilter createHeaderFilter(){
        return new HeaderFilter();
    }

    @Bean
    public JwtTokenInterceptor jwtTokenInterceptor(){
        return new JwtTokenInterceptor(tokenUtils);
    }
}
