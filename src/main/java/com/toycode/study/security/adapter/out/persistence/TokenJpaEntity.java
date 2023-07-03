package com.toycode.study.security.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
class TokenJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tokenSequence;

    @Column(nullable = false, unique = true)
    private String token;

    // NOTE 폐기 및 만료 필드 필요여부 확인
    private Boolean revoked = false;

    private Boolean expired = false;

    @ManyToOne
    @JoinColumn
    private UserJpaEntity user;
}
