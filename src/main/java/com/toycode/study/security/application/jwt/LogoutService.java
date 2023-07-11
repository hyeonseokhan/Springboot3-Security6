package com.toycode.study.security.application.jwt;

import com.toycode.study.security.application.port.in.TokenDeleteUseCase;
import com.toycode.study.security.common.annotation.UseCase;
import com.toycode.study.security.domain.TokenInfo.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@UseCase
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    /**
     * 'Application Layer -> Application Layer' 로 접근하기 위한 인터페이스
     */
    private final TokenDeleteUseCase tokenDeleteUseCase;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        final String authHeader = request.getHeader(JwtFilter.AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(JwtFilter.AUTHORIZATION_HEADER_BEARER)) {
            return;
        }
        Token jwt = Token.of(authHeader.substring(JwtFilter.AUTHORIZATION_HEADER_BEARER.length()));
        tokenDeleteUseCase.revokeIssuedToken(jwt);
        SecurityContextHolder.clearContext();
    }
}
