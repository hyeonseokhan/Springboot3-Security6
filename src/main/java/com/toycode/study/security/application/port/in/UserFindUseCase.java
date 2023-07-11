package com.toycode.study.security.application.port.in;

import com.toycode.study.security.domain.User;

public interface UserFindUseCase {

    /**
     * 사용자 조회 요청
     *
     * @param request 사용자 조회 요청문
     */
    User findUserByUsername(String username);
}
