package com.html.cgmaker.login.form.web.service;

import com.html.cgmaker.login.domain.enums.UserRole;
import com.html.cgmaker.login.form.web.dto.MemberDto;
import com.html.cgmaker.login.form.web.entity.Member;
import com.html.cgmaker.login.form.web.dto.SignUpDto;
import com.html.cgmaker.login.form.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public MemberDto signUp(final SignUpDto signUpDto){
        final Member member = Member.builder()
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .city(signUpDto.getCity())
                .password(bCryptPasswordEncoder.encode(signUpDto.getPassword()))
                .sex(signUpDto.getSex())
                .age(signUpDto.getAge())
                .role(UserRole.USER)
                .build();

        Member saveMember = memberRepository.save(member);
        return MemberDto.builder()
                .name(saveMember.getName())
                .age(saveMember.getAge())
                .city(saveMember.getCity())
                .sex(saveMember.getSex())
                .build();
    }

    public boolean isEmailDuplicated(final String email){
        return memberRepository.existsByEmail(email);
    }

    public Optional<Member> findByEmail(final String email){
        return memberRepository.findByEmail(email);
    }
}
