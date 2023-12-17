package com.example.oauth2login.domain.members.type;

import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum AuthType {

    GOOGLE("google","구글 간편로그인"),
    APPLE("apple","애플 간편 로그인"),
    KAKAO("kakao","카카오 간편로그인");

    private String registrationId;
    private String desc;

    AuthType(String registrationId, String desc) {
        this.registrationId = registrationId;
        this.desc = desc;
    }

    private static final Map<String, AuthType> BY_KEY =
            Stream.of(values())
                    .collect(Collectors.toMap(AuthType::getRegistrationId, Function.identity()));

    public static AuthType valueOfKey(String insertKey) {
        return BY_KEY.get(insertKey);
    }
}
