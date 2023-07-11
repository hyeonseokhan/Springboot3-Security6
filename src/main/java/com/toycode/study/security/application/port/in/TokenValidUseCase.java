package com.toycode.study.security.application.port.in;

import com.toycode.study.security.domain.TokenInfo.Token;
import com.toycode.study.security.domain.User.Username;

public interface TokenValidUseCase {

    /**
     * 입력받은 토큰의 유효상태를 반환한다.
     *
     * @param token    발급된 토큰
     * @param username 토큰 발급자
     * @return 유효상태
     */
    Boolean isValid(Token token, Username username);
}
