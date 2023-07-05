package com.toycode.study.security.domain;

import com.toycode.study.security.domain.User.Username;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Getter
@AllArgsConstructor
public class TokenInformation {

    final Token token;

    final Username username;

    Boolean revoked;

    public void revoke() {
        this.revoked = true;
    }

    @Value
    public static class Token {

        String value;

        public static TokenInformation.Token of(String value) {
            return new Token(value);
        }
    }
}
