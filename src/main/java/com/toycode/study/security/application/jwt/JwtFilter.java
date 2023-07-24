package com.toycode.study.security.application.jwt;

import com.toycode.study.security.application.port.in.TokenParseUseCase;
import com.toycode.study.security.domain.TokenInfo.Token;
import com.toycode.study.security.domain.User.Username;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";

    /**
     * 'Application Layer -> Application Layer' 로 접근하기 위한 인터페이스
     */
    private final TokenParseUseCase tokenParseUseCase;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(AUTHORIZATION_HEADER_BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        Token token = Token.of(authHeader.substring(AUTHORIZATION_HEADER_BEARER.length()));

//        try {
        Username username = tokenParseUseCase.getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username.getValue());
        // TODO 조회된 사용자의 상태를 채크한다.

//        } catch (SecurityException e) {
//            log.debug("{}유효한 JWT 토큰이 없습니다, uri: {}{}",
//                LogUtil.YELLOW, request.getRequestURI(), LogUtil.RESET);
//        }
        filterChain.doFilter(request, response);
    }
}