package com.toycode.study.security.adapter.out.persistence;

import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import org.springframework.stereotype.Component;

@Component
class UserMapper {

    UserJpaEntity toJpaEntity(User user) {
        return UserJpaEntity.builder()
            .username(user.getUsername().getValue())
            .nickname(user.getNickname())
            .password(user.getPassword())
            .authority(user.getAuthority())
            .build();
    }

    User toDomainEntity(UserJpaEntity user) {
        return new User(
            new Username(user.getUsername()),
            user.getNickname(),
            user.getPassword(),
            user.getAuthority()
        );
    }
}
