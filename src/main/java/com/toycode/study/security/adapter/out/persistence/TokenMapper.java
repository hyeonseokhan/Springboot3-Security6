package com.toycode.study.security.adapter.out.persistence;

import com.toycode.study.security.domain.Token;
import com.toycode.study.security.domain.User.Username;
import org.springframework.stereotype.Component;

@Component
class TokenMapper {

    TokenJpaEntity toJpaEntity(Token token) {
        return TokenJpaEntity.builder()
            .token(token.getValue())
            .username(token.getUsername().getValue())
            .revoked(token.getRevoked())
            .expired(token.getExpired())
            .build();
    }

    Token toDomainEntity(TokenJpaEntity token) {
        return new Token(
            token.getToken(),
            Username.of(token.getUsername()),
            token.getRevoked(),
            token.getExpired()
        );
    }
}
