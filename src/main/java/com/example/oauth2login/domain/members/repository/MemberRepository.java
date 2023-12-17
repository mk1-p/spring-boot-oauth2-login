package com.example.oauth2login.domain.members.repository;

import com.example.oauth2login.domain.members.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
}
