package com.example.sample_back.repository.registUser;

import com.example.sample.generated.model.RequestRegistUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@MybatisTest
public class RegistUserRepositoryTest {

    @Autowired
    RegistUserRepository registUserRepository;

    @Test
    @DisplayName("ユーザー登録に成功")
    void successRegistUser() {

        // Given userInfoテーブルはschema.sqlで定義済

        // When
        RequestRegistUser requestRegistUser = new RequestRegistUser("successUserId", "successUserName", "successPassword");
        int resultRegistUser = registUserRepository.registUser(requestRegistUser.getUserId(), requestRegistUser.getUserName(), requestRegistUser.getPassword());

        // Then
        assertThat(resultRegistUser).isEqualTo(1);
    }

    @Test
    @DisplayName("ユーザーIDが重複時")
    void failRegistExistsByUser() {
        // Given userInfoテーブル、データはschema.sql、data.sqlで定義済

        // When/Then
        RequestRegistUser requestRegistUser = new RequestRegistUser("ExistsByUserId1", "ExistsByUserName1", "ExistsByPassword");
        assertThatThrownBy(() -> registUserRepository.registUser(requestRegistUser.getUserId(),
                requestRegistUser.getUserName(), requestRegistUser.getPassword()))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @DisplayName("ユーザー確認チェック")
    void isRegistedUser() {
        // Given userInfoテーブル、データはschema.sql、data.sqlで定義済

        // When
        Integer existUserId = registUserRepository.isRegistedUser("ExistsByUserId1");
        Integer notExistUserId = registUserRepository.isRegistedUser("NotExistsByUserId");

        // Then
        assertThat(existUserId).isEqualTo(1);
        assertThat(notExistUserId).isEqualTo(0);
    }
}
