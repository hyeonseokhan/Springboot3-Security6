package com.toycode.study.security.application.port.in;

import com.toycode.study.security.application.port.dto.UserDeleteRequest;

public interface UserDeleteUseCase {

    /**
     * 사용자 삭제 요청
     *
     * @param request 사용자 삭제 요청문
     */
    void deleteUser(UserDeleteRequest request);
}
