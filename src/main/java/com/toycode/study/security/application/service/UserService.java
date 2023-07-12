package com.toycode.study.security.application.service;

import com.toycode.study.security.application.port.dto.UserDeleteRequest;
import com.toycode.study.security.application.port.dto.UserRegistrationRequest;
import com.toycode.study.security.application.port.in.UserDeleteUseCase;
import com.toycode.study.security.application.port.in.UserFindUseCase;
import com.toycode.study.security.application.port.in.UserRegistrationUseCase;
import com.toycode.study.security.application.port.out.UserPersistencePort;
import com.toycode.study.security.common.annotation.UseCase;
import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class UserService implements
    UserRegistrationUseCase,
    UserFindUseCase,
    UserDeleteUseCase {

    private final UserPersistencePort userPersistencePort;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserRegistrationRequest request) {
        User user = new User(
            Username.of(request.getUsername()),
            request.getNickname(),
            passwordEncoder.encode(request.getPassword()),
            request.getAuthority());

        userPersistencePort.insertUser(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userPersistencePort.findByUsername(Username.of(username));
    }

    @Override
    public void deleteUser(UserDeleteRequest request) {

    }
}
