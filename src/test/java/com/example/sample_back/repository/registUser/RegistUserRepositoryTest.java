package com.example.sample_back.repository.registUser;

import com.example.sampleback.model.RequestRegistUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@MybatisTest
public class RegistUserRepositoryTest {

    @Autowired
    RegistUserRepository registUserRepository;

    @Test
    @DisplayName("ユーザー登録に成功")
    void successRegistUser(){

        // When
        RequestRegistUser requestRegistUser = new RequestRegistUser("successUserId", "successUserName", "successPassword");
        int resultRegistUser = registUserRepository.registUser(requestRegistUser.getUserId(), requestRegistUser.getUserName(), requestRegistUser.getPassword());

        // Then
        assertThat(resultRegistUser).isEqualTo(1);
    }

}
