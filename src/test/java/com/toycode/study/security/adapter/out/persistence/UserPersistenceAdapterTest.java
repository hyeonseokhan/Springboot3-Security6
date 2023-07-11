package com.toycode.study.security.adapter.out.persistence;

import com.toycode.study.security.domain.TestData;
import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({UserPersistenceAdapter.class, UserMapper.class})
class UserPersistenceAdapterTest {

    @Autowired
    private UserPersistenceAdapter adapter;

    @Autowired
    private UserRepository repository;

    private final User user = TestData.getUser();

    @BeforeEach
    @DisplayName("사용자 등록 테스트")
    void register_user() {
        adapter.insertUser(user);
        Assertions.assertTrue(repository.count() > 0);
    }

    @Test
    @DisplayName("사용자 조회 테스트")
    void find_user() {
        User domain = adapter.findByUsername(Username.of(user.getUsername()));
        Assertions.assertEquals(domain.getUsername(), user.getUsername());
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    void delete_user() {
        adapter.deleteUser(Username.of(user.getUsername()));
        Assertions.assertEquals(0, repository.count());
    }
}