package com.toycode.study.security.application.service;

import com.toycode.study.security.application.port.in.TokenDeleteUseCase;
import com.toycode.study.security.application.port.in.TokenGenerationUseCase;
import com.toycode.study.security.application.port.in.TokenParseUseCase;
import com.toycode.study.security.application.port.out.TokenPersistencePort;
import com.toycode.study.security.common.annotation.UseCase;
import com.toycode.study.security.common.exception.SecurityError;
import com.toycode.study.security.common.exception.SecurityException;
import com.toycode.study.security.domain.TokenInfo;
import com.toycode.study.security.domain.TokenInfo.Token;
import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.Valid;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class TokenService implements
    TokenGenerationUseCase,
    TokenParseUseCase,
    TokenDeleteUseCase,
    InitializingBean {

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
        // TODO 구조체 변경
        Map<String, Object> header = new HashMap<>();
        header.put(Header.TYPE, "JWT");

        // TODO 구조체 변경
        Map<String, Object> payload = new HashMap<>();
        String auth = user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        payload.put("auth", auth);

        String jwt = Jwts.builder()
            .setHeader(header)
            .setClaims(payload)
            .setId(UUID.randomUUID().toString())
            .setSubject(user.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(
                new Date(
                    System.currentTimeMillis() + (this.tokenValidityInMilliseconds * 1000)))
            .signWith(key, SignatureAlgorithm.HS512)
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
    public Username getUsernameFromToken(@Valid Token token) throws SecurityException {
        if (!tokenPersistencePort.isValid(token)) {
            throw new SecurityException(SecurityError.INVALID_TOKEN);
        }

        Claims claims = extractClaimsFromToken(token.getValue());
        String subject = extractClaim(claims, Claims::getSubject);
        return Username.of(subject);
    }

    private <T> T extractClaim(Claims claims, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(claims);
    }

    private Claims extractClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();

        } catch (UnsupportedJwtException e) {
            log.error("Token unsupported");
        } catch (MalformedJwtException e) {
            log.error("Invalid Token");
        } catch (SignatureException e) {
            log.error("Invalid Signature");
        } catch (ExpiredJwtException e) {
            log.error("Token expired");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty");
        }
        return claims;
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
