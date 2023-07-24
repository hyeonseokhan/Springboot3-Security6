package com.toycode.study.security.config;

import com.toycode.study.security.application.jwt.ExceptionHandlingFilter;
import com.toycode.study.security.application.jwt.Http403ForbiddenEntryPoint;
import com.toycode.study.security.application.jwt.JwtFilter;
import com.toycode.study.security.application.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
class SecurityFilterChainConfig {

    private final AuthenticationEntryPoint http403ForbiddenEntiryPointCustom;

    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtFilter;
    private final ExceptionHandlingFilter exceptionHandlingFilter;

    private final SecurityProperties properties;

    /**
     * <h1>Security Filter Chain Bean</h1>
     * <h3>CSRF Section</h3>
     * <pre>
     * <a
     * href="https://velog.io/@cdbrouk/Web-Security-CORS-XSS-CSRF"><code>CSRF</code></a> 링크참조
     * </pre>
     *
     * <h3>Login Section</h3>
     * <pre>
     * <a
     * href=""><code>Spring Security Login</code></a> 링크참조
     * </pre>
     *
     * <h3>Session Section</h3>
     * <pre>
     * <a
     * href="https://velog.io/@seongwon97/Spring-Security-%EC%84%B8%EC%85%98-%EA%B4%80%EB%A6%AC"><code>Spring Security Session</code></a> 링크참조
     * </pre>
     *
     * <h3>Exception Section</h3>
     * <pre>
     * # 403 에러처리
     * {@link Http403ForbiddenEntryPoint}
     *
     * # Handling Filter
     * {@link ExceptionHandlingFilter}
     * </pre>
     *
     * <h3>Authentication Section</h3>
     * <pre>
     * # JWT Filter 구현
     * {@link com.toycode.study.security.application.jwt.JwtFilter}
     * </pre>
     *
     * <h3>Logout Section</h3>
     * <pre>
     * # LogoutHandler 구현
     * {@link LoginService}
     * # <a href="https://velog.io/@dailylifecoding/spring-security-logout-feature"><code>Spring Security Logout</code></a> 링크참조
     * </pre>
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // CSRF Section
            .csrf().disable()

            // Login Section
            .formLogin().disable()

            // Session Section
            // NOTE 추후 '다중 로그인 제한' 기능을 구현시 필요.
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // Exception Section
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(http403ForbiddenEntiryPointCustom)

            // Authentication Section
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlingFilter, JwtFilter.class);

        // Exclude API Path
        for (String path : properties.getExclude().getApiPaths()) {
            http.authorizeHttpRequests().requestMatchers(path).permitAll();
        }
        return http.build();
    }
}
