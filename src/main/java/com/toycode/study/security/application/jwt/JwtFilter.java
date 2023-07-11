package com.toycode.study.security.application.jwt;

import com.toycode.study.security.application.port.in.TokenParseUseCase;
import com.toycode.study.security.application.port.in.TokenValidUseCase;
import com.toycode.study.security.common.LogUtil;
import com.toycode.study.security.domain.TokenInfo.Token;
import com.toycode.study.security.domain.User.Username;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
    private final TokenValidUseCase tokenValidUseCase;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(AUTHORIZATION_HEADER_BEARER)) {
            log.debug("{}요청 메세지에 JWT 토큰이 없습니다, uri: {}{}",
                LogUtil.YELLOW, request.getRequestURI(), LogUtil.RESET);
            filterChain.doFilter(request, response);
            return;
        }

        Token jwt = Token.of(authHeader.substring(AUTHORIZATION_HEADER_BEARER.length()));
        Username username = tokenParseUseCase.getUsernameFromToken(jwt);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username.getValue());
            if (tokenValidUseCase.isValid(jwt, username)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("{}security context 에 '{}' 인증 정보를 저장했습니다, uri: {}{}",
                    LogUtil.GREEN, authToken.getName(), request.getRequestURI(), LogUtil.RESET);
            }
        } else {
            log.debug("{}유효한 JWT 토큰이 없습니다, uri: {}{}",
                LogUtil.YELLOW, request.getRequestURI(), LogUtil.RESET);
        }
        filterChain.doFilter(request, response);
    }
}