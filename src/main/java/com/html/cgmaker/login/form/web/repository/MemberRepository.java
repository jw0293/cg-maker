package com.html.cgmaker.login.form.web.repository;

import com.html.cgmaker.login.form.web.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmailAndPassword(String email, String password);
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

}
