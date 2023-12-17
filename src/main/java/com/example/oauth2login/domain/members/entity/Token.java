package com.example.oauth2login.domain.members.entity;

import com.example.oauth2login.domain.common.BaseEntity;
import com.example.oauth2login.domain.members.type.AuthType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Token extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "token_id")
    private UUID id;
    private String serviceRefreshToken;
    private String authId;
    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Token(UUID id, String serviceRefreshToken, String authId, AuthType authType, Member member) {
        this.id = id;
        this.serviceRefreshToken = serviceRefreshToken;
        this.authId = authId;
        this.authType = authType;
        this.member = member;
    }
}
