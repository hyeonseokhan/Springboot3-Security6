package com.toycode.study.security.application.service;

import com.toycode.study.security.application.port.in.TokenDeleteUseCase;
import com.toycode.study.security.application.port.in.TokenGenerationUseCase;
import com.toycode.study.security.application.port.out.TokenPersistencePort;
import com.toycode.study.security.common.annotation.UseCase;
import com.toycode.study.security.domain.Token;
import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

@UseCase
@RequiredArgsConstructor
public class TokenProvider implements
    TokenGenerationUseCase,
    TokenDeleteUseCase,
    InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";

    /**
     * 'Application Layer -> Infrastructure Layer' 로 접근하기 위한 인터페이스
     */
    private final TokenPersistencePort tokenPersistencePort;

    @Value("${app.security.jwt.secret}")
    private String secret;

    @Value("${app.security.jwt.token-validity-in-seconds}")
    private long tokenValidityInMilliseconds;

    private Key key;

    /**
     * <a href="https://datatracker.ietf.org/doc/html/rfc7519"><code>JWT-RFC7519</code></a>를 준수하여
     * 토큰을 생성한다.
     *
     * @param user 사용자 계정
     * @return 문자열 토큰(ECDSA512-Alg)
     */
    @Override
    public Token createToken(User user) {
        // 새로운 jwt 생성
        String claim = user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        String jwt = Jwts.builder()
            .setSubject(user.getUsername())
            .claim(AUTHORITIES_KEY, claim)
            .signWith(key, SignatureAlgorithm.HS512)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(
                new Date(
                    System.currentTimeMillis() + (this.tokenValidityInMilliseconds * 1000)))
            .compact();

        // 토큰 발행
        Token token = Token.issue(jwt, user.getUsername());



        // 토큰 저장
        tokenPersistencePort.saveToken(token);

        return token;
    }

    @Override
    public void deleteIssuedTokenOfUser(User user) {
        List<Token> tokens = tokenPersistencePort.findAllValidTokenByUsername(
            Username.of(user.getUsername()));
        if (tokens.isEmpty()) {
            return;
        }
        tokens.forEach(token -> {
            // TODO 중간 커밋
        });
    }

    @Override
    public void afterPropertiesSet() {
        Optional.ofNullable(secret)
            .ifPresent(s -> {
                byte[] keyBytes = Decoders.BASE64.decode(s);
                key = Keys.hmacShaKeyFor(keyBytes);
            });
    }
}
