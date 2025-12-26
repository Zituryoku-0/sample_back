package com.example.sample_back.service.registUser;

import com.example.sample_back.repository.registUser.RegistUserRepository;
import com.example.sampleback.model.RequestRegistUser;
import com.example.sampleback.model.SuccessRegistUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistUserServiceTest {

    @Mock
    private RegistUserRepository repository;

    @InjectMocks
    private RegistUserService service;

    @Test
    @DisplayName("ユーザー登録に成功する場合のテスト")
    void successRegistUser() {
        RequestRegistUser requestRegistUser = new RequestRegistUser("successUserId", "successUserName",
                "successPassword");

        when(repository.isRegistedUser("successUserId")).thenReturn(0);
        when(repository.registUser("successUserId", "successUserName", "successPassword"))
                .thenReturn(1);

        SuccessRegistUser result = service.regist(requestRegistUser);

        assertThat(result.getResponseInfo().getCode()).isEqualTo("201");
        assertThat(result.getResponseInfo().getMessage()).isEqualTo("success");
        assertThat(result.getData().getUserId()).isEqualTo("successUserId");
        assertThat(result.getData().getUserName()).isEqualTo("successUserName");
        assertThat(result.getData().getLoginCheck()).isTrue();
        assertThat(result.getData().getMessage()).isEqualTo("ユーザー登録に成功しました。");

        verify(repository, times(1)).isRegistedUser("successUserId");
        verify(repository, times(1)).registUser("successUserId",
                "successUserName", "successPassword");
    }
}
