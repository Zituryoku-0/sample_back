package com.example.sample_back.controller.user;

import com.example.sample.generated.model.SuccessLogin;
import com.example.sample.generated.model.SuccessLoginUser;
import com.example.sample.generated.model.SuccessResponseInfo;
import com.example.sample_back.service.login.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoginService loginService;

    @Nested
    @DisplayName("正常系テスト")
    class SuccessTests {

        @Test
        @DisplayName("正しいuserIdとpasswordでログイン成功（200 OK）")
        void loginSuccess_withValidCredentials() throws Exception {
            // Given
            SuccessLoginUser successLoginUser = new SuccessLoginUser();
            SuccessResponseInfo successResponseInfo = new SuccessResponseInfo();
            SuccessLogin successLogin = new SuccessLogin();
            successResponseInfo.setCode("200");
            successResponseInfo.setMessage("success");
            successLogin.setUserId("successUserId");
            successLogin.setUserName("テストユーザー");
            successLogin.setLoginCheck(true);
            successLogin.setMessage("ログインに成功しました。");
            successLoginUser.setResponseInfo(successResponseInfo);
            successLoginUser.setData(successLogin);
            when(loginService.find(any())).thenReturn(successLoginUser);

            Map<String, String> request = new HashMap<>();
            request.put("userId", "successUserId");
            request.put("password", "testPassword");

            // When & Then
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.responseInfo.code").value("200"))
                    .andExpect(jsonPath("$.responseInfo.message").value("success"))
                    .andExpect(jsonPath("$.data.userId").value("successUserId"))
                    .andExpect(jsonPath("$.data.userName").value("テストユーザー"))
                    .andExpect(jsonPath("$.data.loginCheck").value(true))
                    .andExpect(jsonPath("$.data.message").value("ログインに成功しました。"));
        }
    }

    @Nested
    @DisplayName("異常系テスト")
    class FailureTests {

        @Test
        @DisplayName("存在しないユーザーでのログイン失敗")
        void loginFailure_withNonExistentUser() throws Exception {

            // Given
            SuccessLoginUser successLoginUser = new SuccessLoginUser();
            SuccessResponseInfo successResponseInfo = new SuccessResponseInfo();
            SuccessLogin successLogin = new SuccessLogin();
            successResponseInfo.setCode("200");
            successResponseInfo.setMessage("success");
            successLogin.setUserId("");
            successLogin.setUserName("");
            successLogin.setLoginCheck(false);
            successLogin.setMessage("ログインに失敗しました。ユーザーIDまたはパスワードが正しくありません。");
            successLoginUser.setResponseInfo(successResponseInfo);
            successLoginUser.setData(successLogin);
            when(loginService.find(any())).thenReturn(successLoginUser);

            Map<String, String> request = new HashMap<>();
            request.put("userId", "nonExistentUser");
            request.put("password", "testPassword");

            // When & Then
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.responseInfo.code").value("200"))
                    .andExpect(jsonPath("$.responseInfo.message").value("success"))
                    .andExpect(jsonPath("$.data.userId").value(""))
                    .andExpect(jsonPath("$.data.userName").value(""))
                    .andExpect(jsonPath("$.data.loginCheck").value(false))
                    .andExpect(jsonPath("$.data.message")
                            .value("ログインに失敗しました。ユーザーIDまたはパスワードが正しくありません。"));
        }

        @Test
        @DisplayName("必須パラメータ（userId）の欠如")
        void loginFailure_withMissingUserId() throws Exception {
            // Given
            Map<String, String> request = new HashMap<>();
            request.put("password", "testPassword");

            // When & Then
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("必須パラメータ（password）の欠如")
        void loginFailure_withMissingPassword() throws Exception {
            // Given
            Map<String, String> request = new HashMap<>();
            request.put("userId", "testUser");

            // When & Then
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("複数ユーザーヒット時の処理（IllegalArgumentException）")
        void loginFailure_withMultipleUsersFound() throws Exception {
            // Given
            when(loginService.find(any()))
                    .thenThrow(new IllegalArgumentException("複数のユーザーが該当しました。"));

            Map<String, String> request = new HashMap<>();
            request.put("userId", "duplicateUser");
            request.put("password", "testPassword");

            // When & Then
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(jsonPath("$.responseInfo.code").value("400"))
                    .andExpect(jsonPath("$.responseInfo.message").value("fail"))
                    .andExpect(jsonPath("$.data.userId").value(""))
                    .andExpect(jsonPath("$.data.userName").value(""))
                    .andExpect(jsonPath("$.data.loginCheck").value(false))
                    .andExpect(jsonPath("$.data.message").value("複数のユーザーが該当しました。"));
        }

        @Test
        @DisplayName("予期しない例外発生時の処理")
        void loginFailure_withUnexpectedException() throws Exception {
            // Given
            when(loginService.find(any()))
                    .thenThrow(new RuntimeException("サーバー内部でエラーが発生しました。"));

            Map<String, String> request = new HashMap<>();
            request.put("userId", "testUser");
            request.put("password", "testPassword");

            // When & Then
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.responseInfo.code").value("500"))
                    .andExpect(jsonPath("$.responseInfo.message").value("fail"))
                    .andExpect(jsonPath("$.data.userId").value(""))
                    .andExpect(jsonPath("$.data.userName").value(""))
                    .andExpect(jsonPath("$.data.loginCheck").value(false))
                    .andExpect(jsonPath("$.data.message").value("サーバー内部でエラーが発生しました。"));
        }
    }
}
