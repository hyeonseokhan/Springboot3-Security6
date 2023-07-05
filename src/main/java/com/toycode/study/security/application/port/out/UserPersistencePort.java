package com.toycode.study.security.application.port.out;

import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;

/**
 * 사용자 엔티티의 Persistence 요청 인터페이스
 */
public interface UserPersistencePort {

    /**
     * 사용자 등록 요청
     *
     * @param user 신규 사용자 정보
     */
    void insertUser(User user);

    /**
     * 사용자명 검색조건에 맞는 단일 사용자를 조회한다.
     *
     * @param username 사용자명
     * @return 사용자 도메인
     */
    User findByUsername(Username username);

    /**
     * 사용자 삭제 요청
     *
     * @param username 삭제할 사용자명
     */
    void deleteUser(Username username);
}
