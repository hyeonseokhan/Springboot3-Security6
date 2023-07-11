package com.toycode.study.security.config;

import com.toycode.study.security.application.jwt.ExceptionHandlerFilter;
import com.toycode.study.security.application.jwt.JwtFilter;
import com.toycode.study.security.application.service.LoginService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
class FilterChainConfig {

    @Value("${app.api.version}")
    private String API_VERSION;

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final LogoutHandler logoutHandler;

    private final JwtFilter jwtFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final SecurityProperties properties;

    private final AuthenticationProvider authenticationProvider;

    /**
     * <h1>Security Filter Chain Bean</h1>
     * <h3>Session Section</h3>
     * <pre>
     * <a
     * href="https://velog.io/@seongwon97/Spring-Security-%EC%84%B8%EC%85%98-%EA%B4%80%EB%A6%AC"><code>SpringSecurity Session</code></a> 링크참조
     * </pre>
     *
     * <h3>Exception Section</h3>
     * <pre>
     *     # 401 에러처리
     *     {@link com.toycode.study.security.application.jwt.AuthenticationEntryPointImpl}
     *
     *     # 403 에러처리
     *     {@link com.toycode.study.security.application.jwt.AccessDeniedHandlerImpl}
     * </pre>
     *
     * <h3>Authentication Section</h3>
     * <pre>
     *     # JWT Filter 구현
     *     {@link com.toycode.study.security.application.jwt.JwtFilter}
     * </pre>
     *
     * <h3>Logout Section</h3>
     * <pre>
     *     # LogoutHandler 구현
     *     {@link LoginService}
     * </pre>
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()

            .formLogin().disable()
            .headers().frameOptions().disable()

            // Session Section
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // Exception Section
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler)

            // Authentication Section
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlerFilter, JwtFilter.class)

            // Logout Section
            .logout()
            .logoutUrl(API_VERSION + "/auth/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler(
                (request, response, authentication) -> SecurityContextHolder.clearContext());

        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requests = http.authorizeHttpRequests();

        Optional.ofNullable(properties.getExclude().getWebPaths())
            .map(list -> list.isEmpty() ? null : list)
            .ifPresent(list -> requests.requestMatchers(list.toArray(new String[0])).permitAll());
        requests.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
        requests.requestMatchers(PathRequest.toH2Console()).permitAll();

        Optional.ofNullable(properties.getExclude().getApiPaths())
            .map(a -> a.isEmpty() ? null : a)
            .orElseGet(() -> Collections.singletonList(Collections.singletonList("/**")))
            .forEach(e -> {
                HttpMethod method = getMethod(e);
                requests.requestMatchers(method, e.get(0)).permitAll();
            });
        requests.anyRequest().permitAll();

        return http.build();
    }

    private HttpMethod getMethod(List<String> path) {
        return path.size() == 1 ||
            (path.size() > 1 && "ALL".equalsIgnoreCase(path.get(1))) ?
            null : HttpMethod.valueOf(path.get(1));
    }
}
