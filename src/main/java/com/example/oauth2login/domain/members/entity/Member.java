package com.example.oauth2login.domain.members.entity;

import com.example.oauth2login.domain.common.BaseEntity;
import com.example.oauth2login.domain.members.type.RoleType;
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
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "member_id")
    private UUID id;
    @Column(length = 30)
    private String email;
    @Column(length = 11)
    private String phone;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToOne(mappedBy = "member")
    private Token token;


    @Builder
    public Member(UUID id, String email, String phone, RoleType role, Token token) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.token = token;
    }
}
