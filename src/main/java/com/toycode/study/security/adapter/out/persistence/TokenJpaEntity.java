package com.toycode.study.security.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "token")
class TokenJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tokenSequence;

    @Column(nullable = false, length = 500, unique = true)
    private String token;

    @Column(nullable = false)
    private String username;

    // NOTE 폐기 및 만료 필드 필요여부 확인
    private Boolean revoked = false;

    private Boolean expired = false;
}
