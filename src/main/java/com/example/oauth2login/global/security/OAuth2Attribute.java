package com.example.oauth2login.global.security;


import com.example.oauth2login.domain.members.type.AuthType;
import com.example.oauth2login.domain.members.type.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OAuth2Attribute {

    private Map<String, Object> attributes;     // Attributes 원본
    private String nameAttributeKey;            // attribute key : google(sub), kakao(id)
    private String attributeId;                 // 고유 id
    private String name;
    private String email;
    private String picture;
    private String registrationId;              // 간편로그인 서비스 구분 (ex : google, kakao)
    private RoleType role;                      // 권한 값
    private AuthType auth;


    @Builder
    public OAuth2Attribute(Map<String, Object> attributes, String nameAttributeKey, String attributeId,
                           String name, String email, String picture, String registrationId,
                           RoleType role, AuthType auth) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.attributeId = attributeId;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.registrationId = registrationId;
        this.role = role != null ? role : RoleType.USER;
        this.auth = auth;
    }


    public  static OAuth2Attribute of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        AuthType auth = AuthType.valueOf(registrationId);

        OAuth2Attribute attribute;

        // 제공 타입에 따른 객체 세팅
        switch (auth) {
            case GOOGLE -> attribute = ofGoogle(userNameAttributeName, attributes);
            case KAKAO -> attribute = ofKakao(userNameAttributeName, attributes);
            case APPLE -> attribute =  null;
            default -> attribute = null;
        }

        return attribute;

    }

    private static OAuth2Attribute ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .attributeId(String.valueOf(attributes.get(userNameAttributeName)))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .registrationId(AuthType.GOOGLE.getRegistrationId())
                .auth(AuthType.GOOGLE)
                .build();
    }
    private static OAuth2Attribute ofKakao(String userNameAttributeName, Map<String, Object> attributes) {

        // 카카오의 계정 정보 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        // 카카오의 프로필 정보 추출
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .attributes(attributes)
                .attributeId(String.valueOf(attributes.get(userNameAttributeName)))
                .nameAttributeKey(userNameAttributeName)
                .name((String) profile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .registrationId(AuthType.KAKAO.getRegistrationId())
                .auth(AuthType.KAKAO)
                .build();
    }

}
