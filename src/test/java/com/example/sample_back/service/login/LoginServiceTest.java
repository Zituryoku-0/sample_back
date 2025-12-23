package com.example.sample_back.service.login;

import com.example.sample_back.exception.UnauthorizedException;
import com.example.sample_back.repository.login.UserRecord;
import com.example.sample_back.repository.login.UserRepository;
import com.example.sampleback.model.RequestLogin;
import com.example.sampleback.model.SuccessLoginUser;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserRecord userRecord;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("ユーザーが存在する場合、トリムされたUserEntityを返す")
    void find_whenUserExists_returnsTrimmedUserEntity() {
        RequestLogin request = new RequestLogin("  uid  ", " pass ");

        when(repository.selectUser("  uid  ", " pass ")).thenReturn(userRecord);
        when(userRecord.getUserId()).thenReturn(" uid ");
        when(userRecord.getUserName()).thenReturn(" name ");

        SuccessLoginUser result = loginService.find(request);

        assertNotNull(result.getResponseInfo());
        assertNotNull(result.getData());
        assertThat(result.getResponseInfo().getCode()).isEqualTo("200");
        assertThat(result.getResponseInfo().getMessage()).isEqualTo("success");
        assertThat(result.getData().getUserId()).isEqualTo("uid");
        assertThat(result.getData().getUserName()).isEqualTo("name");
        assertThat(result.getData().getLoginCheck()).isTrue();
        assertThat(result.getData().getMessage()).isEqualTo("ログインに成功しました。");

        verify(repository, times(1)).selectUser("  uid  ", " pass ");
    }

    @Test
    @DisplayName("ユーザー不正の場合、401ステータスコードと空のUserEntityを返す")
    void find_whenUserNotFound_returnsEmptyUserEntity() {
        RequestLogin request = new RequestLogin("nope", "nopass");

        when(repository.selectUser("nope", "nopass"))
                .thenThrow(new UnauthorizedException("ログインに失敗しました。ユーザーIDまたはパスワードが正しくありません。"));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> loginService.find(request));
        assertThat(ex.getMessage()).contains("ログインに失敗しました。ユーザーIDまたはパスワードが正しくありません。");

        verify(repository, times(1)).selectUser("nope", "nopass");
    }

    @Test
    @DisplayName("複数のユーザーが見つかった場合、IllegalArgumentExceptionをスローする")
    void find_whenTooManyResults_throwsIllegalArgumentException() {
        RequestLogin request = new RequestLogin("dup", "p");
        when(repository.selectUser("dup", "p")).thenThrow(new TooManyResultsException("too many"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> loginService.find(request));
        assertThat(ex.getMessage()).contains("複数のユーザーが該当しました。");

        verify(repository, times(1)).selectUser("dup", "p");
    }

    @Test
    @DisplayName("予期しない例外が発生した場合、RuntimeExceptionをスローする")
    void find_whenOtherException_throwsRuntimeException() {
        RequestLogin request = new RequestLogin("err", "p");
        when(repository.selectUser("err", "p")).thenThrow(new RuntimeException("db error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> loginService.find(request));
        assertTrue(ex.getMessage().contains("サーバー内部でエラーが発生しました。"));
        verify(repository, times(1)).selectUser("err", "p");
    }
}