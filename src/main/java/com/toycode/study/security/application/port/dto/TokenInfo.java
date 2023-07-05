package com.toycode.study.security.application.port.dto;

import com.toycode.study.security.domain.Token;
import lombok.Value;

/**
 * 발급된 토큰 정보객체
 */
@Value
public class TokenInfo {

    String access_token;

    public static TokenInfo of(Token token) {
        return new TokenInfo(token.getValue());
    }
}
