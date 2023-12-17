package com.example.oauth2login.domain.members.repository;

import com.example.oauth2login.domain.members.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
}
