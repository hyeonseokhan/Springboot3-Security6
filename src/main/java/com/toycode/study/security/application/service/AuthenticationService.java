package com.toycode.study.security.application.service;

import com.toycode.study.security.application.port.dto.LoginRequest;
import com.toycode.study.security.application.port.dto.TokenInfo;
import com.toycode.study.security.application.port.in.LoginUseCase;
import com.toycode.study.security.common.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@UseCase
@RequiredArgsConstructor
public class AuthenticationService implements
    LoginUseCase {

    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenInfo login(LoginRequest request) {
        return null;
    }
}
