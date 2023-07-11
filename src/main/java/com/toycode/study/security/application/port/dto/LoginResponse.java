package com.toycode.study.security.application.port.dto;

import com.toycode.study.security.domain.TokenInfo;
import lombok.Value;

/**
 * 토큰 요청 응답문
 */
@Value
public class LoginResponse {

    String access_token;

    public static LoginResponse of(TokenInfo tokenInfo) {
        return new LoginResponse(tokenInfo.getToken().getValue());
    }
}
