package com.example.sample_back.service.login;

import ch.qos.logback.core.util.StringUtil;
import com.example.sample_back.repository.login.UserRecord;
import com.example.sample_back.repository.login.UserRepository;
import com.example.sampleback.model.RequestLogin;
import com.example.sampleback.model.SuccessLogin;
import com.example.sampleback.model.SuccessLoginUser;
import com.example.sampleback.model.SuccessResponseInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository repository;

    public SuccessLoginUser find(RequestLogin request) {
        // レスポンスフォーマット
        SuccessLoginUser successLoginUser = new SuccessLoginUser();
        // レスポンスのresponseInfo部分
        SuccessResponseInfo successResponseInfo = new SuccessResponseInfo();
        // レスポンスのdata部分
        SuccessLogin successLogin = new SuccessLogin();
        try {
            boolean loginCheck = false;
            String userId = "";
            String userName = "";
            UserRecord resSelect = repository.selectUser(request.getUserId(), request.getPassword());
            if (!StringUtil.isNullOrEmpty(resSelect.getUserId()) && !StringUtil.isNullOrEmpty(resSelect.getUserName()) ) {
                userId = resSelect.getUserId().trim();
                userName = resSelect.getUserName().trim();
                loginCheck = true;
            }
            String message = loginCheck ? "ログインに成功しました。" : "ログインに失敗しました。ユーザーIDまたはパスワードが正しくありません。";
            successResponseInfo.setCode("200");
            successResponseInfo.setMessage("success");
            successLogin.setUserId(userId);
            successLogin.setUserName(userName);
            successLogin.setLoginCheck(loginCheck);
            successLogin.setMessage(message);

            successLoginUser.setResponseInfo(successResponseInfo);
            successLoginUser.setData(successLogin);
            return successLoginUser;
        } catch (TooManyResultsException tooManyResultsException) {
            // ユーザーIDが複数件ヒットした場合は、例外を返す
            log.error("Too many results returned for userId = {}", request.getUserId(), tooManyResultsException);
            throw new IllegalArgumentException("複数のユーザーが該当しました。");
        } catch (Exception e) {
            log.error("An exception has occurred", e);
            throw new RuntimeException("サーバー内部でエラーが発生しました。");
        }
    }
}
