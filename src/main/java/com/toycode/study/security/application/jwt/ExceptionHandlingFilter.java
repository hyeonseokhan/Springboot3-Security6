package com.toycode.study.security.application.jwt;

import com.toycode.study.security.common.exception.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * Security Filter Chain
 */
@Slf4j
@Component
public class ExceptionHandlingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws IOException {
        ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(request);
        try {
            filterChain.doFilter(cachingRequest, response);
        } catch (SecurityException e) {
            send(response, e.getMessage(), e.getError().getHttpStatus());
        } catch (ServletException e) {
            send(response, e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    private void send(HttpServletResponse response, String message, HttpStatus status)
        throws IOException {
        log.warn("message: {}, status: {}", message, status);

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(status.value());

        PrintWriter writer = response.getWriter();
        writer.write(message);
        writer.flush();
    }
}
