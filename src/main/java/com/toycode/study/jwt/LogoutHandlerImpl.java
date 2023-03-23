package com.toycode.study.jwt;

import com.toycode.study.entity.Token;
import com.toycode.study.repository.TokenRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        final String authHeader = request.getHeader(JwtFilter.AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(JwtFilter.AUTHORIZATION_HEADER_BEARER)) {
            return;
        }
        final String jwt = authHeader.substring(JwtFilter.AUTHORIZATION_HEADER_BEARER.length());
        Token storedToken = tokenRepository.findByToken(jwt)
            .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
