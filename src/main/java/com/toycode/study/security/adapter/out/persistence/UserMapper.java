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
            .password(user.getPassword()) // NOTE 암호화된 패스워드 값이어야 한다.
            .authority(user.getAuthority())
            .build();
    }

    User toDomainEntity(UserJpaEntity user) {
        return new User(
            new Username(user.getUsername()),
            user.getNickname(),
            user.getPassword(), // NOTE 암호화된 패스워드를 반환한다.
            user.getAuthority()
        );
    }
}
