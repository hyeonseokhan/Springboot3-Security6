package com.toycode.study.security.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends
    UserRepositoryCustom,
    JpaRepository<UserJpaEntity, Long> {

    /**
     * 사용자명을 검색조건으로 사용자를 조회
     *
     * @param username 사용자명
     * @return 사용자
     */
    Optional<UserJpaEntity> findByUsername(String username);

    /**
     * 사용자명을 검색조건으로 사용자를 삭제
     * @param username 사용자명
     */
    void deleteByUsername(String username);
}
