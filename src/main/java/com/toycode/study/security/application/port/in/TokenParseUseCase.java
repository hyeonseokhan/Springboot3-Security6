package com.toycode.study.security.application.port.in;

import com.toycode.study.security.domain.TokenInfo.Token;
import com.toycode.study.security.domain.User.Username;

public interface TokenParseUseCase {

    /**
     * 입력받은 토큰값을 검증하고 토큰 소유자명을 반환한다.
     *
     * @param token 토큰값
     * @return 토큰 소유자명
     * @throws com.toycode.study.security.common.exception.SecurityException 토큰검증에 실패
     */
    Username getUsernameFromToken(Token token) throws SecurityException;
}
