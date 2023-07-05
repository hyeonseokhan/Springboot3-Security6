package com.toycode.study.security.application.port.in;

import com.toycode.study.security.domain.User;

public interface TokenDeleteUseCase {

    /**
     * 해당 사용자가 이미 발급한 토큰 목록을 만료시킨다.
     * @param user 사용자
     */
    void deleteIssuedTokenOfUser(User user);
}
