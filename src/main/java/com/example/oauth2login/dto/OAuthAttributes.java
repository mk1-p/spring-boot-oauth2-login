package com.example.oauth2login.dto;


import com.example.oauth2login.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@ToString
@Slf4j
public class OAuthAttributes {
    private Map<String, Object> attributes;     // Attributes 원본
    private String nameAttributeKey;            // attribute key : google(sub), kakao(id)
    private String attributeId;                 // 고유 id
    private String name;
    private String email;
    private String picture;
    private String registrationId;              // 간편로그인 서비스 구분 (ex : google, kakao)
    private Role role;                          // 권한 값

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String attributeId, String name, String email, String picture,
                           String registrationId, Role role) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.attributeId = attributeId;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.registrationId = registrationId;
        this.role = role != null ? role : Role.COMMON;
    }

    public  static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        // 제공 타입에 따른 객체 세팅
        if (registrationId.equals("google")) {
            return ofGoogle(userNameAttributeName, attributes, registrationId);
        } else if (registrationId.equals("kakao")) {
            return ofKakao(userNameAttributeName, attributes, registrationId);
        }

        return null;

    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes, String registrationId) {
        return OAuthAttributes.builder()
                .attributeId(String.valueOf(attributes.get(userNameAttributeName)))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .registrationId(registrationId)
                .build();
    }
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes, String registrationId) {

        // 카카오의 계정 정보 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        // 카카오의 프로필 정보 추출
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .attributes(attributes)
                .attributeId(String.valueOf(attributes.get(userNameAttributeName)))
                .nameAttributeKey(userNameAttributeName)
                .name((String) profile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .registrationId(registrationId)
                .build();
    }

}
