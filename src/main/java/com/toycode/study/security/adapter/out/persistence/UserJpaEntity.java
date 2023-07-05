package com.toycode.study.security.adapter.out.persistence;

import com.toycode.study.security.domain.Authority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "_user")
class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userSequence;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isEnabled = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;
}
