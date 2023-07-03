package com.toycode.study.security.application.port.in;

import com.toycode.study.security.application.port.dto.UserRegistrationRequest;

public interface UserRegistrationUseCase {

    /**
     * 사용자 등록 요청
     *
     * @param request 사용자 등록 요청문
     */
    void registerUser(UserRegistrationRequest request);
}
