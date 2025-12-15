package com.example.sample_back.controller.registUser;

import com.example.sample_back.exception.FailureRegistUserException;
import com.example.sample_back.service.registUser.RegistUserService;
import com.example.sampleback.model.RequestRegistUser;
import com.example.sampleback.model.ResponseSuccessRegistUser;
import com.example.sampleback.model.SuccessRegistUser;
import com.example.sampleback.model.SuccessResponseInfo;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistUserController.class)
public class RegistUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegistUserService registUserService;


    @Nested
    @DisplayName("ユーザー登録成功")
    class TestSuccessRegistUser {

        @Test
        void successRegistUser() throws Exception {
            // Given
            SuccessResponseInfo responseInfo = new SuccessResponseInfo("201", "success");
            ResponseSuccessRegistUser data = new ResponseSuccessRegistUser("successUserId", "successUserName", true,"ユーザー登録に成功しました。");
            SuccessRegistUser successRegistUser = new SuccessRegistUser();
            successRegistUser.setResponseInfo(responseInfo);
            successRegistUser.setData(data);
            RequestRegistUser requestRegistUser = new RequestRegistUser("successUserId", "successUserName", "successPassword");
            when(registUserService.regist(requestRegistUser)).thenReturn(successRegistUser);

            Map<String, String> request = new HashMap<>();
            request.put("userId", "successUserId");
            request.put("userName", "successUserName");
            request.put("password", "successPassword");

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.responseInfo.code").value("201"))
                    .andExpect(jsonPath("$.responseInfo.message").value("success"))
                    .andExpect(jsonPath("$.data.userId").value("successUserId"))
                    .andExpect(jsonPath("$.data.userName").value("successUserName"))
                    .andExpect(jsonPath("$.data.loginCheck").value(true))
                    .andExpect(jsonPath("$.data.message").value("ユーザー登録に成功しました。"));
        }
    }

    @Nested
    @DisplayName("ユーザー登録失敗")
    class FailRegistUser {
        @Test
        @DisplayName("ユーザー登録失敗")
        void failRegistUser() throws Exception {
            // Given
            RequestRegistUser requestRegistUser = new RequestRegistUser("failureUserId", "failureUserName", "failurePassword");
            when(registUserService.regist(requestRegistUser)).thenThrow(new FailureRegistUserException("ユーザー登録に失敗しました。"));

            Map<String, String> request = new HashMap<>();
            request.put("userId", "failureUserId");
            request.put("userName", "failureUserName");
            request.put("password", "failurePassword");

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(jsonPath("$.responseInfo.code").value("500"))
                    .andExpect(jsonPath("$.responseInfo.message").value("fail"))
                    .andExpect(jsonPath("$.data.userId").value(""))
                    .andExpect(jsonPath("$.data.userName").value(""))
                    .andExpect(jsonPath("$.data.loginCheck").value(false))
                    .andExpect(jsonPath("$.data.message").value("ユーザー登録に失敗しました。"));
        }

        @Test
        @DisplayName("ユーザーIDが重複時")
        void failRegistUserId() throws Exception {
            // Given
            RequestRegistUser requestRegistUser = new RequestRegistUser("failureUserId", "failureUserName", "failurePassword");
            when(registUserService.regist(requestRegistUser)).thenThrow(new IllegalArgumentException("ユーザーIDが重複しています。"));

            Map<String, String> request = new HashMap<>();
            request.put("userId", "failureUserId");
            request.put("userName", "failureUserName");
            request.put("password", "failurePassword");

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(jsonPath("$.responseInfo.code").value("400"))
                    .andExpect(jsonPath("$.responseInfo.message").value("fail"))
                    .andExpect(jsonPath("$.data.userId").value(""))
                    .andExpect(jsonPath("$.data.userName").value(""))
                    .andExpect(jsonPath("$.data.loginCheck").value(false))
                    .andExpect(jsonPath("$.data.message").value("ユーザーIDが重複しています。"));
        }

        @Test
        @DisplayName("その他実行時エラー")
        void failInternalError() throws Exception {
            // Given
            RequestRegistUser requestRegistUser = new RequestRegistUser("failureUserId", "failureUserName", "failurePassword");
            when(registUserService.regist(requestRegistUser)).thenThrow(new RuntimeException("サーバー内部でエラーが発生しました。"));

            Map<String, String> request = new HashMap<>();
            request.put("userId", "failureUserId");
            request.put("userName", "failureUserName");
            request.put("password", "failurePassword");

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(jsonPath("$.responseInfo.code").value("500"))
                    .andExpect(jsonPath("$.responseInfo.message").value("fail"))
                    .andExpect(jsonPath("$.data.userId").value(""))
                    .andExpect(jsonPath("$.data.userName").value(""))
                    .andExpect(jsonPath("$.data.loginCheck").value(false))
                    .andExpect(jsonPath("$.data.message").value("サーバー内部でエラーが発生しました。"));
        }
    }

    @Nested
    @DisplayName("バリデーションエラー")
    class ValidationErrorTests {

        @Test
        @DisplayName("userIdが空文字の場合")
        void testEmptyUserId() throws Exception {
            // Given
            Map<String, String> request = new HashMap<>();
            request.put("userId", "");
            request.put("userName", "validUserName");
            request.put("password", "validPassword");

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.responseInfo.code").value("400"))
                    .andExpect(jsonPath("$.responseInfo.message").value("fail"));
        }

        @Test
        @DisplayName("userIdが長すぎる場合（33文字）")
        void testUserIdTooLong() throws Exception {
            // Given
            Map<String, String> request = new HashMap<>();
            request.put("userId", "a".repeat(33)); // 33文字
            request.put("userName", "validUserName");
            request.put("password", "validPassword");

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.responseInfo.code").value("400"))
                    .andExpect(jsonPath("$.responseInfo.message").value("fail"));
        }

        @Test
        @DisplayName("userNameが空文字の場合")
        void testEmptyUserName() throws Exception {
            // Given
            Map<String, String> request = new HashMap<>();
            request.put("userId", "validUserId");
            request.put("userName", "");
            request.put("password", "validPassword");

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.responseInfo.code").value("400"))
                    .andExpect(jsonPath("$.responseInfo.message").value("fail"));
        }

        @Test
        @DisplayName("userNameが長すぎる場合（65文字）")
        void testUserNameTooLong() throws Exception {
            // Given
            Map<String, String> request = new HashMap<>();
            request.put("userId", "validUserId");
            request.put("userName", "a".repeat(65)); // 65文字
            request.put("password", "validPassword");

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.responseInfo.code").value("400"))
                    .andExpect(jsonPath("$.responseInfo.message").value("fail"));
        }

        @Test
        @DisplayName("passwordが空文字の場合")
        void testEmptyPassword() throws Exception {
            // Given
            Map<String, String> request = new HashMap<>();
            request.put("userId", "validUserId");
            request.put("userName", "validUserName");
            request.put("password", "");

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.responseInfo.code").value("400"))
                    .andExpect(jsonPath("$.responseInfo.message").value("fail"));
        }

        @Test
        @DisplayName("passwordが長すぎる場合（65文字）")
        void testPasswordTooLong() throws Exception {
            // Given
            Map<String, String> request = new HashMap<>();
            request.put("userId", "validUserId");
            request.put("userName", "validUserName");
            request.put("password", "a".repeat(65)); // 65文字

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.responseInfo.code").value("400"))
                    .andExpect(jsonPath("$.responseInfo.message").value("fail"));
        }

        @Test
        @DisplayName("必須フィールドがnullの場合")
        void testNullFields() throws Exception {
            // Given
            Map<String, String> request = new HashMap<>();
            // すべてのフィールドをnullにする（リクエストに含めない）

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.responseInfo.code").value("400"))
                    .andExpect(jsonPath("$.responseInfo.message").value("fail"));
        }

        @Test
        @DisplayName("境界値テスト：userIdが32文字（最大値）")
        void testUserIdMaxLength() throws Exception {
            // Given
            SuccessResponseInfo responseInfo = new SuccessResponseInfo("201", "success");
            ResponseSuccessRegistUser data = new ResponseSuccessRegistUser("a".repeat(32), "validUserName", true,"ユーザー登録に成功しました。");
            SuccessRegistUser successRegistUser = new SuccessRegistUser();
            successRegistUser.setResponseInfo(responseInfo);
            successRegistUser.setData(data);
            RequestRegistUser requestRegistUser = new RequestRegistUser("a".repeat(32), "validUserName", "validPassword");
            when(registUserService.regist(requestRegistUser)).thenReturn(successRegistUser);

            Map<String, String> request = new HashMap<>();
            request.put("userId", "a".repeat(32)); // 32文字（最大）
            request.put("userName", "validUserName");
            request.put("password", "validPassword");

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.responseInfo.code").value("201"));
        }

        @Test
        @DisplayName("境界値テスト：userIdが1文字（最小値）")
        void testUserIdMinLength() throws Exception {
            // Given
            SuccessResponseInfo responseInfo = new SuccessResponseInfo("201", "success");
            ResponseSuccessRegistUser data = new ResponseSuccessRegistUser("a", "validUserName", true,"ユーザー登録に成功しました。");
            SuccessRegistUser successRegistUser = new SuccessRegistUser();
            successRegistUser.setResponseInfo(responseInfo);
            successRegistUser.setData(data);
            RequestRegistUser requestRegistUser = new RequestRegistUser("a", "validUserName", "validPassword");
            when(registUserService.regist(requestRegistUser)).thenReturn(successRegistUser);

            Map<String, String> request = new HashMap<>();
            request.put("userId", "a"); // 1文字（最小）
            request.put("userName", "validUserName");
            request.put("password", "validPassword");

            // When & Then
            mockMvc.perform(post("/registUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.responseInfo.code").value("201"));
        }
    }
}
