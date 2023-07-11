package com.toycode.study.security.domain;

import com.toycode.study.security.domain.User.Username;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Getter
@AllArgsConstructor
public class TokenInfo {

    final Token token;

    final Username username;

    Boolean revoked;

    Boolean expired;

    /**
     * 발급된 토큰으로 토큰정보를 생성한다.
     *
     * @param value    발급된 토큰 값
     * @param username 토큰 생성자
     */
    public TokenInfo(String value, String username) {
        this.token = Token.of(value);
        this.username = Username.of(username);
        this.revoked = false;
        this.expired = false;
    }

    /**
     * 발급된 토큰 상태를 '만료' 시킨다.
     */
    public TokenInfo expire() {
        this.expired = true;
        return this;
    }

    /**
     * 발급된 토큰 상태를 '폐기' 시킨다.
     */
    public TokenInfo revoke() {
        this.expired = true;
        this.revoked = true;
        return this;
    }

    @Value
    public static class Token {

        @NotBlank
        String value;

        public static Token of(String value) {
            return new Token(value);
        }
    }
}
