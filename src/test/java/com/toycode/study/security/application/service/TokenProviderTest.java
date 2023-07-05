package com.toycode.study.security.application.service;

import com.toycode.study.security.domain.Authority;
import com.toycode.study.security.domain.Token;
import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Test
    @DisplayName("토큰 발급 테스트")
    void generate_token() {
        // Given
        tokenProvider.afterPropertiesSet();
        User user = new User(
            new Username("toycode"),
            "토이코드",
            "$2a$10$p3HID6F2/OcBi06VCRSCWO4dCfk5wdhjDheMrVOjoMh7NYcLzBGri",
            Authority.MANAGER);

        // When
        Token token = tokenProvider.createToken(user);

        // Then
        Assertions.assertNotNull(token.getValue());
        System.out.printf("==> JWT: %s", token);
    }
}