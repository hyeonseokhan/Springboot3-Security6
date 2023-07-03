package com.toycode.study.security.application.port.out;

import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;

public interface UserFindPort {

    /**
     * 사용자명 검색조건에 맞는 단일 사용자를 조회한다.
     *
     * @param username 사용자명
     * @return 사용자 도메인
     */
    User findByUsername(Username username);
}
