package com.toycode.study.security.adapter.out.persistence;

import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import org.springframework.stereotype.Component;

@Component
class UserMapper {

    UserJpaEntity toJpaEntity(User user) {
        return UserJpaEntity.builder()
            .username(user.getUsername())
            .nickname(user.getNickname())
            .password(user.getPassword()) // 암호화된 패스워드
            .authority(user.getAuthority())
            .isEnabled(user.isEnabled())
            .build();
    }

    User toDomainEntity(UserJpaEntity user) {
        return new User(
            Username.of(user.getUsername()),
            user.getNickname(),
            user.getPassword(), // 암호화된 패스워드
            user.getAuthority()
        );
    }
}
