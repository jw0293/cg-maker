package com.html.cgmaker.login.form.web.service;

import com.html.cgmaker.login.form.web.entity.MyUserDetails;
import com.html.cgmaker.login.form.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class MemberDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(u -> new MyUserDetails(u, Collections.singleton(new SimpleGrantedAuthority(u.getRole().getKey()))))
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
