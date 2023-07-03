package com.toycode.study.security.adapter.out.persistence;

import com.toycode.study.security.application.port.out.UserDeletePort;
import com.toycode.study.security.application.port.out.UserFindPort;
import com.toycode.study.security.application.port.out.UserSavePort;
import com.toycode.study.security.common.PersistenceAdapter;
import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
class UserPersistenceAdapter implements
    UserSavePort,
    UserFindPort,
    UserDeletePort {

    private final UserRepository repository;

    private final UserMapper mapper;

    @Override
    public void insertUser(User user) {
        repository.save(mapper.toJpaEntity(user));
    }

    @Override
    public User findByUsername(Username username) {
        UserJpaEntity user = repository.findByUsername(username.getValue()).orElseThrow();
        return mapper.toDomainEntity(user);
    }

    @Override
    public void deleteUser(Username username) {
        repository.deleteByUsername(username.getValue());
    }
}
