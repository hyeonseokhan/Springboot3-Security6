package com.toycode.study.security.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

/**
 * 사용자 권한 클래스
 */
public enum Authority {
    // @formatter:off
    ROOT_MANAGER,   // 최고 관리자
    MANAGER,        // 중간 관리자
    USER            // 일반 유저
    ;
    // @formatter:on

    @JsonCreator
    public static Authority parsing(String value) {
        return Stream.of(Authority.values())
            .filter(authority -> authority.toString().equals(value.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("`%s` is invalid value.", value)));
    }
}
