package com.toycode.study.security.application.service;

import com.toycode.study.security.application.port.in.UserDeleteUseCase;
import com.toycode.study.security.application.port.in.UserRegistrationUseCase;
import com.toycode.study.security.application.port.in.dto.UserDeleteRequest;
import com.toycode.study.security.application.port.in.dto.UserRegistrationRequest;
import com.toycode.study.security.application.port.out.UserDeletePort;
import com.toycode.study.security.application.port.out.UserSavePort;
import com.toycode.study.security.common.UseCase;
import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UserService implements
    UserRegistrationUseCase,
    UserDeleteUseCase {

    private final UserSavePort userSavePort;
    private final UserDeletePort userDeletePort;

    @Override
    public void registerUser(UserRegistrationRequest request) {
        User user = new User(
            new Username(request.getUsername()),
            request.getNickname(),
            request.getPassword(), // TODO 암호화는 도메인 클래스에서 상태변경으로 진행할 것인가?
            request.getAuthority());

        userSavePort.insertUser(user);
    }

    @Override
    public void deleteUser(UserDeleteRequest request) {

    }
}
