package com.toycode.study.security.application.jwt;

import com.toycode.study.security.common.exception.SecurityError;
import com.toycode.study.security.common.exception.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * <p>
 * In the pre-authenticated authentication case (unlike CAS, for example) the user will already have
 * been identified through some external mechanism and a secure context established by the time the
 * security-enforcement filter is invoked.
 * <p>
 * Therefore this class isn't actually responsible for the commencement of authentication, as it is
 * in the case of other providers. It will be called if the user is rejected by the
 * AbstractPreAuthenticatedProcessingFilter, resulting in a null authentication.
 * <p>
 * The <code>commence</code> method will always return an
 * <code>HttpServletResponse.SC_FORBIDDEN</code> (403 error).
 *
 * @author Luke Taylor
 * @author Ruud Senden
 * @see org.springframework.security.web.access.ExceptionTranslationFilter
 * @since 2.0
 */
@Slf4j
@Component
public class Http403ForbiddenEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) {
        log.warn("Pre-authenticated entry point called. Rejecting access");
        throw new SecurityException(SecurityError.FORBIDDEN, authException);
    }
}
