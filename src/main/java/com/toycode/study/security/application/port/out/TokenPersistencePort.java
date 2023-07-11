package com.toycode.study.security.application.port.out;

import com.toycode.study.security.domain.TokenInfo;
import com.toycode.study.security.domain.TokenInfo.Token;
import com.toycode.study.security.domain.User.Username;
import java.util.List;

/**
 * 토큰 엔티티의 Persistence 요청 인터페이스
 */
public interface TokenPersistencePort {

    /**
     * 발행된 토큰을 저장한다.
     *
     * @param tokenInfo 발행된 토큰
     */
    void saveToken(TokenInfo tokenInfo);

    /**
     * 발행된 모든 토큰을 저장한다.
     *
     * @param tokenInfos 발행된 모든 토큰
     */
    void saveAllToken(List<TokenInfo> tokenInfos);

    /**
     * 입력받은 토큰의 유효상태를 반환한다.
     *
     * @param token 발급된 토큰
     * @return 유효상태
     */
    Boolean isValid(Token token);

    /**
     * 입력받은 토큰의 정보를 반환한다.
     *
     * @param token 발급된 토큰
     * @return 토큰정보
     */
    TokenInfo findByToken(Token token);

    /**
     * 입력받은 사용자명의 발급된 토큰목록을 조회한다.
     *
     * @param username 토큰 발급 사용자명
     * @return 유요한 토큰목록
     */
    List<TokenInfo> findAllValidTokenByUsername(Username username);
}
