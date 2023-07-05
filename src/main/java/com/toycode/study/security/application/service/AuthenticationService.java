package com.toycode.study.security.application.service;

import com.toycode.study.security.application.port.dto.LoginRequest;
import com.toycode.study.security.application.port.dto.TokenInfo;
import com.toycode.study.security.application.port.in.LoginUseCase;
import com.toycode.study.security.application.port.in.TokenDeleteUseCase;
import com.toycode.study.security.application.port.in.TokenGenerationUseCase;
import com.toycode.study.security.application.port.out.UserPersistencePort;
import com.toycode.study.security.common.annotation.UseCase;
import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@UseCase
@RequiredArgsConstructor
public class AuthenticationService implements
    LoginUseCase {

    /**
     * 'Application Layer -> Application Layer' 로 접근하기 위한 인터페이스
     */
    private final TokenGenerationUseCase tokenGenerationUseCase;
    private final TokenDeleteUseCase tokenDeleteUseCase;

    /**
     * 'Application Layer -> Infrastructure Layer' 로 접근하기 위한 인터페이스
     */
    private final UserPersistencePort userPersistencePort;

    private final AuthenticationManager authenticationManager;

    @Override
    public TokenInfo login(LoginRequest request) {
        // NOTE 아래의 코드 기능 학습필요.
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userPersistencePort.findByUsername(Username.of(request.getUsername()));

        tokenDeleteUseCase.deleteIssuedTokenOfUser(user);

        return TokenInfo.of(tokenGenerationUseCase.createToken(user));
    }
}
