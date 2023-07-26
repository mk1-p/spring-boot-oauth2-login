package com.example.oauth2login.model;

import lombok.Getter;

@Getter
public enum Role {

    GUEST("ROLE_GUEST","비 로그인 사용자"),
    COMMON("ROLE_USER","일반 사용자"),
    API("ROLE_API","API Key 사용자"),
    VIP("ROLE_VIP","특별 사용자"),
    ADMIN("ROLE_ADMIN","관리자");


    private String key;
    private String desc;

    Role(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
