package com.toycode.study.security.domain;

import com.toycode.study.security.domain.User.Username;
import lombok.Value;

//@Getter
//@AllArgsConstructor
@Value
public class Token {

    String value;

    Username username;

    Boolean revoked;

    Boolean expired;

    /**
     * 새로운 토큰 발행
     *
     * @param token    문자열 토큰
     * @param username 토큰 발생 대상
     * @return 토큰 정보
     */
    public static Token issue(String value, String username) {
        return new Token(value, Username.of(username), false, false);
    }
}
