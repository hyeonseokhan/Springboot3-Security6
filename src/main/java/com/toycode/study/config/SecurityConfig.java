package com.toycode.study.config;

import com.toycode.study.jwt.ExceptionHandlerFilter;
import com.toycode.study.jwt.JwtFilter;
import com.toycode.study.util.LogUtil;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final LogoutHandler logoutHandler;

    private final SecurityProperties prop;

//    @Bean
//    public WebSecurityCustomizer ignoringCustomizer() {
//        return (web) -> {
//            WebSecurity.IgnoredRequestConfigurer ignoring = web.ignoring();
//            Optional.ofNullable(prop.getExclude().getWebPaths())
//                .map(list -> list.isEmpty() ? null : list)
//                .ifPresent(list -> ignoring.requestMatchers(list.toArray(new String[0])));
//            ignoring.requestMatchers(PathRequest.toH2Console());
//            ignoring.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//        };
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("{}---> SecurityConfig.filterChain{}",
            LogUtil.GREEN, LogUtil.RESET);
        http
            .csrf().disable()

            .formLogin().disable()
            .headers().frameOptions().disable()

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler)

            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlerFilter, JwtFilter.class)

            .logout()
            .logoutUrl("/api/v1/auth/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler(
                (request, response, authentication) -> SecurityContextHolder.clearContext());

        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requests = http.authorizeHttpRequests();

        Optional.ofNullable(prop.getExclude().getWebPaths())
            .map(list -> list.isEmpty() ? null : list)
            .ifPresent(list -> requests.requestMatchers(list.toArray(new String[0])).permitAll());
        requests.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
        requests.requestMatchers(PathRequest.toH2Console()).permitAll();

        Optional.ofNullable(prop.getExclude().getApiPaths())
            .map(a -> a.isEmpty() ? null : a)
            .orElseGet(() -> Collections.singletonList(Collections.singletonList("/**")))
            .forEach(e -> {
                HttpMethod method = getMethod(e);
                requests.requestMatchers(method, e.get(0)).permitAll();
            });
//        requests.anyRequest().authenticated();
        requests.anyRequest().permitAll();
        return http.build();
    }

    private HttpMethod getMethod(List<String> path) {
        return path.size() == 1 ||
            (path.size() > 1 && "ALL".equalsIgnoreCase(path.get(1))) ?
            null : HttpMethod.valueOf(path.get(1));
    }
}
