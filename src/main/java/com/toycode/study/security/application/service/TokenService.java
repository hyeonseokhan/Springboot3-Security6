package com.toycode.study.security.application.service;

import com.toycode.study.security.application.port.in.TokenDeleteUseCase;
import com.toycode.study.security.application.port.in.TokenGenerationUseCase;
import com.toycode.study.security.application.port.in.TokenParseUseCase;
import com.toycode.study.security.application.port.in.TokenValidUseCase;
import com.toycode.study.security.application.port.out.TokenPersistencePort;
import com.toycode.study.security.common.annotation.UseCase;
import com.toycode.study.security.common.exception.InvalidTokenException;
import com.toycode.study.security.domain.TokenInfo;
import com.toycode.study.security.domain.TokenInfo.Token;
import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class TokenService implements
    TokenGenerationUseCase,
    TokenValidUseCase,
    TokenParseUseCase,
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
    @Transactional(rollbackFor = Exception.class)
    public TokenInfo createToken(User user) {
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

        // 토큰 정보 생성
        TokenInfo token = new TokenInfo(jwt, user.getUsername());

        // 토큰 저장
        tokenPersistencePort.saveToken(token);

        return token;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revokeIssuedToken(Token token) {
        TokenInfo tokenInfo = tokenPersistencePort.findByToken(token);
        tokenInfo.revoke();
        tokenPersistencePort.saveToken(tokenInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revokeIssuedTokenOfUser(User user) {
        List<TokenInfo> tokens = tokenPersistencePort
            .findAllValidTokenByUsername(Username.of(user.getUsername()));

        if (!tokens.isEmpty()) {
            List<TokenInfo> list = tokens.stream()
                .map(TokenInfo::revoke)
                .toList();
            tokenPersistencePort.saveAllToken(list);
        }
    }

    @Override
    public Boolean isValid(Token token, Username username) {
        if (!tokenPersistencePort.isValid(token)) {
            throw new InvalidTokenException("유효한 토큰 목록 중 해당 토큰 값이 존재하지 않습니다.");
        }
        Username parsedUsername = getUsernameFromToken(token);
        if (!username.equals(parsedUsername)) {
            throw new InvalidTokenException("토큰 내 발급자 정보와 입력받은 발급자가 동일하지 않습니다.");
        }
        if (isTokenExpired(token)) {
            throw new InvalidTokenException("해당 토큰이 만료되었습니다.");
        }
        return true;
    }

    @Override
    public Username getUsernameFromToken(@Valid Token token) {
        return Username.of(extractClaim(token.getValue(), Claims::getSubject));
    }

    private boolean isTokenExpired(@Valid Token token) {
        return extractClaim(token.getValue(), Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();
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
