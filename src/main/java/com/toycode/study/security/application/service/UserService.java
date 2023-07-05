package com.toycode.study.security.application.service;

import com.toycode.study.security.application.port.dto.UserDeleteRequest;
import com.toycode.study.security.application.port.dto.UserRegistrationRequest;
import com.toycode.study.security.application.port.in.UserDeleteUseCase;
import com.toycode.study.security.application.port.in.UserRegistrationUseCase;
import com.toycode.study.security.application.port.out.UserPersistencePort;
import com.toycode.study.security.common.annotation.UseCase;
import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@UseCase
@RequiredArgsConstructor
public class UserService implements
    UserRegistrationUseCase,
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
    public void deleteUser(UserDeleteRequest request) {

    }
}
