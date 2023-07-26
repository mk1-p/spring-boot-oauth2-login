package com.example.oauth2login.member;

import com.example.oauth2login.dto.OAuthAttributes;
import com.example.oauth2login.model.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "MEMBER",uniqueConstraints = @UniqueConstraint(
        columnNames = {"attribute_id", "registration_id"}
))
@NoArgsConstructor
@Getter
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attribute_id")
    private String attributeId;             // 간편로그인 계정의 고유 ID
    @Column(name = "registration_id")
    private String registrationId;          // 간편로그인 고유 계정 (ex, google, kakao)
    @Column
    private String name;                    // attribute name
    @Column
    private String email;                   // attribute email



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;                      // 권한 Role



    @Builder
    public Member(Long id, String attributeId, String registrationId, String name, String email, Role role) {
        this.id = id;
        this.attributeId = attributeId;
        this.registrationId = registrationId;
        this.name = name;
        this.email = email;
        this.role = role;
    }



    public static Member toEntity(OAuthAttributes attributes) {
        return Member.builder()
                .attributeId(attributes.getAttributeId())
                .registrationId(attributes.getRegistrationId())
                .name(attributes.getName())
                .email(attributes.getEmail())
                .role(attributes.getRole())
                .build();

    }

    public Member updateNameAndEmail(String name, String email) {
        this.name = name;
        this.email = email;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }



}
