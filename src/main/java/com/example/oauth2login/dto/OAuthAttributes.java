package com.example.oauth2login.dto;


import com.example.oauth2login.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

@Getter
@ToString
@Slf4j
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String attributeId;
    private String name;
    private String email;
    private String picture;
    private String registrationId;
    private Role role;

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

        // 카카오로 받은 데이터에서 계정 정보가 담긴 kakao_account 값을 꺼낸다.
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        // 마찬가지로 profile(nickname, image_url.. 등) 정보가 담긴 값을 꺼낸다.
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
