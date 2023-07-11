package com.toycode.study.security.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface TokenRepository extends
    JpaRepository<TokenJpaEntity, Long> {

    @Query(value = """
        select t from TokenJpaEntity t\s
        where t.token = :token\s
        and (t.expired = false or t.revoked = false)\s
        """)
    Optional<TokenJpaEntity> findValidToken(String token);

    @Query(value = """
        select t from TokenJpaEntity t\s
        where t.username = :username\s
        and (t.expired = false or t.revoked = false)\s
        """)
    List<TokenJpaEntity> findAllValidTokenByUsername(String username);
}
