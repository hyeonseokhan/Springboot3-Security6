package com.toycode.study.security.application.port.in;

import com.toycode.study.security.domain.TokenInfo;
import com.toycode.study.security.domain.User;

public interface TokenGenerationUseCase {

    /**
     * 토큰을 생성요청을 한다. JWT 토큰생성의 구체적인 구현방법은 제한두지 않는다.
     *
     * @param user 사용자 계정
     * @return JWT
     */
    TokenInfo createToken(User user);
}
