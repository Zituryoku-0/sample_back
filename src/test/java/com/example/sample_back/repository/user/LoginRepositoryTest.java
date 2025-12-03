package com.example.sample_back.repository.user;

import com.example.sample_back.repository.login.UserRecord;
import com.example.sample_back.repository.login.UserRepository;
import com.example.sampleback.model.RequestLogin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@MybatisTest
class LoginRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("ユーザー情報取得のテスト")
    void testSelectUser() {

        // Given:テストデータ（H2に投入）

        // When
        RequestLogin requestLogin = new RequestLogin("sampleUserId1", "abcdef");
        UserRecord userRecord = userRepository.selectUser(requestLogin.getUserId(), requestLogin.getPassword());

        // Then
        assertThat(userRecord.getUserId().trim()).isEqualTo("sampleUserId1");
        assertThat(userRecord.getUserName().trim()).isEqualTo("sample UserName1");
    }
}
