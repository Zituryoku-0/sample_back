package com.example.sample_back.repository.login;

import com.example.sampleback.model.RequestLogin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

@MybatisTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("ユーザー情報取得のテスト")
    void testSelectUser() {

        // Given: テストデータは data.sql で自動投入済み

        // When
        RequestLogin requestLogin = new RequestLogin("sampleUserId1", "abcdef");
        UserRecord userRecord = userRepository.selectUser(requestLogin.getUserId(), requestLogin.getPassword());

        // Then
        assertThat(userRecord.getUserId().trim()).isEqualTo("sampleUserId1");
        assertThat(userRecord.getUserName().trim()).isEqualTo("sample UserName1");
    }

    @Test
    @DisplayName("削除フラグが立っているユーザーの場合、nullを返す")
    void testSelectUser_deletedUser(){
        RequestLogin requestLogin = new RequestLogin("NotLoginUserId", "NotLoginUserPassword");
        UserRecord userRecord = userRepository.selectUser(
                requestLogin.getUserId(),
                requestLogin.getPassword()
        );

        assertThat(userRecord).isNull();
    }

    @Test
    @DisplayName("存在しないユーザーIDの場合、nullを返す")
    void testSelectUser_notFound(){
        RequestLogin requestLogin = new RequestLogin("nonExistentUser", "password");
        UserRecord userRecord = userRepository.selectUser(
                requestLogin.getUserId(),
                requestLogin.getPassword()
        );

        assertThat(userRecord).isNull();
    }

    @Test
    @DisplayName("パスワード不一致の場合、nullを返す")
    void testSelectUser_wrongPassword() {
        RequestLogin requestLogin = new RequestLogin("sampleUserId1", "wrongPassword");
        UserRecord userRecord = userRepository.selectUser(
                requestLogin.getUserId(),
                requestLogin.getPassword()
        );

        assertThat(userRecord).isNull();
    }
}
