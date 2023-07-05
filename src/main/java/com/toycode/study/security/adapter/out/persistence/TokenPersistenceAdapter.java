package com.toycode.study.security.adapter.out.persistence;

import com.toycode.study.security.application.port.out.TokenPersistencePort;
import com.toycode.study.security.common.annotation.PersistenceAdapter;
import com.toycode.study.security.domain.Token;
import com.toycode.study.security.domain.User.Username;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
class TokenPersistenceAdapter implements
    TokenPersistencePort {

    private final TokenRepository tokenRepository;

    private final TokenMapper tokenMapper;

    @Override
    public void saveToken(Token token) {
        tokenRepository.save(tokenMapper.toJpaEntity(token));
    }

    @Override
    public List<Token> findAllValidTokenByUsername(Username username) {
        return tokenRepository.findAllValidTokenByUsername(username.getValue())
            .stream()
            .map(tokenMapper::toDomainEntity)
            .collect(Collectors.toList());
    }
}
