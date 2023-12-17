package com.example.oauth2login.domain.members.type;


import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum RoleType {

    USER("ROLE_USER","일반 사용자"),
    ADMIN("ROLE_ADMIN","관리자");


    private String key;
    private String desc;

    RoleType(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getSubKey() {
        return this.getKey().replace("ROLE_","");
    }

    private static final Map<String, RoleType> BY_KEY =
            Stream.of(values())
                    .collect(Collectors.toMap(RoleType::getKey, Function.identity()));

    public static RoleType valueOfKey(String insertKey) {
        return BY_KEY.get(insertKey);
    }



}
