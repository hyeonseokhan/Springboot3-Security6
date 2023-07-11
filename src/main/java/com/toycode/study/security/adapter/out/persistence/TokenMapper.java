package com.toycode.study.security.adapter.out.persistence;

import com.toycode.study.security.domain.TokenInfo;
import com.toycode.study.security.domain.TokenInfo.Token;
import com.toycode.study.security.domain.User.Username;
import org.springframework.stereotype.Component;

@Component
class TokenMapper {

    TokenJpaEntity toJpaEntity(TokenInfo tokenInfo) {
        return TokenJpaEntity.builder()
            .token(tokenInfo.getToken().getValue())
            .username(tokenInfo.getUsername().getValue())
            .revoked(tokenInfo.getRevoked())
            .expired(tokenInfo.getExpired())
            .build();
    }

    TokenInfo toDomainEntity(TokenJpaEntity token) {
        return new TokenInfo(
            Token.of(token.getToken()),
            Username.of(token.getUsername()),
            token.getRevoked(),
            token.getExpired()
        );
    }
}
