package com.example.myshop.repository;

import com.example.myshop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String membername);
    List<Member> findAllByVerifiedTrue();
}
