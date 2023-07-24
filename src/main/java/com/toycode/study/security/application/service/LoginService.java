package com.toycode.study.security.application.service;

import com.toycode.study.security.application.jwt.JwtFilter;
import com.toycode.study.security.application.port.dto.LoginRequest;
import com.toycode.study.security.application.port.dto.LoginResponse;
import com.toycode.study.security.application.port.in.LoginUseCase;
import com.toycode.study.security.application.port.in.TokenDeleteUseCase;
import com.toycode.study.security.application.port.in.TokenGenerationUseCase;
import com.toycode.study.security.application.port.out.UserPersistencePort;
import com.toycode.study.security.common.LogUtil;
import com.toycode.study.security.common.annotation.UseCase;
import com.toycode.study.security.domain.TokenInfo.Token;
import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class LoginService implements
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
    public LoginResponse login(LoginRequest request) {
        User user = userPersistencePort.findByUsername(Username.of(request.getUsername()));

        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken
                    .unauthenticated(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            // TODO 사용자 인증 결과에 따른 도메인 상태변경 반영
            e.printStackTrace();
        }

        tokenDeleteUseCase.revokeIssuedTokenOfUser(user);

        return LoginResponse.of(tokenGenerationUseCase.createToken(user));
    }

    @Override
    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader(JwtFilter.AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(JwtFilter.AUTHORIZATION_HEADER_BEARER)) {
            log.debug("{}로그인 상태의 요청이 아닙니다.{}", LogUtil.YELLOW, LogUtil.RESET);
            return;
        }
        Token jwt = Token.of(authHeader.substring(JwtFilter.AUTHORIZATION_HEADER_BEARER.length()));
        tokenDeleteUseCase.revokeIssuedToken(jwt);
    }
}
