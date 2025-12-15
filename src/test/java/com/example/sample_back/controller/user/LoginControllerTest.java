package com.example.sample_back.controller.user;

import com.example.sample_back.service.login.LoginService;
import com.example.sample_back.service.login.UserEntity;
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
            UserEntity userEntity = new UserEntity("testUser", "テストユーザー", true);
            when(loginService.find(any())).thenReturn(userEntity);

            Map<String, String> request = new HashMap<>();
            request.put("userId", "testUser");
            request.put("password", "testPassword");

            // When & Then
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value("testUser"))
                    .andExpect(jsonPath("$.userName").value("テストユーザー"))
                    .andExpect(jsonPath("$.loginCheck").value(true));
        }
    }

    @Nested
    @DisplayName("異常系テスト")
    class FailureTests {

        @Test
        @DisplayName("存在しないユーザーでのログイン失敗")
        void loginFailure_withNonExistentUser() throws Exception {
            // Given
            UserEntity userEntity = new UserEntity("", "", false);
            when(loginService.find(any())).thenReturn(userEntity);

            Map<String, String> request = new HashMap<>();
            request.put("userId", "nonExistentUser");
            request.put("password", "testPassword");

            // When & Then
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(""))
                    .andExpect(jsonPath("$.userName").value(""))
                    .andExpect(jsonPath("$.loginCheck").value(false));
        }

        @Test
        @DisplayName("パスワード誤りでのログイン失敗")
        void loginFailure_withWrongPassword() throws Exception {
            // Given
            UserEntity userEntity = new UserEntity("", "", false);
            when(loginService.find(any())).thenReturn(userEntity);

            Map<String, String> request = new HashMap<>();
            request.put("userId", "testUser");
            request.put("password", "wrongPassword");

            // When & Then
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(""))
                    .andExpect(jsonPath("$.userName").value(""))
                    .andExpect(jsonPath("$.loginCheck").value(false));
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
