package com.toycode.study.security.application.port.out;

import com.toycode.study.security.domain.User;

public interface UserDeletePort {

    /**
     * 사용자 삭제 요청
     *
     * @param username 삭제할 사용자명
     */
    void deleteUser(User.Username username);
}
