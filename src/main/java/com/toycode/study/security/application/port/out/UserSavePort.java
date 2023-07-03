package com.toycode.study.security.application.port.out;

import com.toycode.study.security.domain.User;

public interface UserSavePort {

    /**
     * 사용자 등록 요청
     *
     * @param user 신규 사용자 정보
     */
    void insertUser(User user);
}
