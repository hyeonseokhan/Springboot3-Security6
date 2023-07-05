package com.toycode.study.security.application.port.out;

import com.toycode.study.security.domain.Token;
import com.toycode.study.security.domain.User.Username;
import java.util.List;

/**
 * 토큰 엔티티의 Persistence 요청 인터페이스
 */
public interface TokenPersistencePort {

    /**
     * 발행된 토큰을 저장한다.
     *
     * @param token 발행된 토큰
     */
    void saveToken(Token token);

    /**
     * 입력받은 사용자명의 발급된 토큰목록을 조회한다.
     *
     * @param username 토큰 발급 사용자명
     * @return 유요한 토큰목록
     */
    List<Token> findAllValidTokenByUsername(Username username);
}
