package com.toycode.study.security.application.port.in;

import com.toycode.study.security.domain.TokenInfo.Token;
import com.toycode.study.security.domain.User;

public interface TokenDeleteUseCase {

    /**
     * 입력받은 토큰을 조회하여 만료상태로 변경한다.
     * @param token 발급된 토큰
     */
    void revokeIssuedToken(Token token);

    /**
     * 해당 사용자가 이미 발급한 토큰 목록을 만료시킨다.
     * @param user 사용자
     */
    void revokeIssuedTokenOfUser(User user);
}
