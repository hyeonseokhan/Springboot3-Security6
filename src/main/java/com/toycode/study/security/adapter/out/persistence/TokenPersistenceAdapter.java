package com.toycode.study.security.adapter.out.persistence;

import com.toycode.study.security.application.port.out.TokenPersistencePort;
import com.toycode.study.security.common.annotation.PersistenceAdapter;
import com.toycode.study.security.domain.TokenInfo;
import com.toycode.study.security.domain.TokenInfo.Token;
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
    public void saveToken(TokenInfo tokenInfo) {
        tokenRepository.save(tokenMapper.toJpaEntity(tokenInfo));
    }

    @Override
    public void saveAllToken(List<TokenInfo> tokenInfos) {
        List<TokenJpaEntity> list = tokenInfos.stream()
            .map(tokenMapper::toJpaEntity)
            .toList();
        tokenRepository.saveAll(list);
    }

    @Override
    public Boolean isValid(Token token) {
        return tokenRepository.findValidToken(token.getValue()).isPresent();
    }

    @Override
    public TokenInfo findByToken(Token token) {
        TokenJpaEntity tokenJpaEntity = tokenRepository.findValidToken(token.getValue())
            .orElseThrow();
        return tokenMapper.toDomainEntity(tokenJpaEntity);
    }

    @Override
    public List<TokenInfo> findAllValidTokenByUsername(Username username) {
        return tokenRepository.findAllValidTokenByUsername(username.getValue())
            .stream()
            .map(tokenMapper::toDomainEntity)
            .collect(Collectors.toList());
    }
}
