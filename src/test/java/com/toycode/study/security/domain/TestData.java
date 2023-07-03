package com.toycode.study.security.domain;

import com.toycode.study.security.domain.User.Username;

public class TestData {

    public static User getUser() {
        return  new User(
            new Username("dreamsecurity"),
            "드림시큐리티",
            "dream1004",
            Authority.MANAGER);
    }
}
