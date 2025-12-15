package com.example.sample_back.service.registUser;

import com.example.sample_back.exception.FailureRegistUserException;
import com.example.sample_back.repository.registUser.RegistUserRepository;
import com.example.sampleback.model.RequestRegistUser;
import com.example.sampleback.model.ResponseSuccessRegistUser;
import com.example.sampleback.model.SuccessRegistUser;
import com.example.sampleback.model.SuccessResponseInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RegistUserService {

    private final RegistUserRepository registUserRepository;


    public SuccessRegistUser regist(RequestRegistUser requestRegistUser) {
        SuccessResponseInfo successResponseInfo = new SuccessResponseInfo();
        ResponseSuccessRegistUser responseSuccessRegistUser = new ResponseSuccessRegistUser();
        SuccessRegistUser successRegistUser = new SuccessRegistUser();
        try {
            // 登録しようとするユーザーIDが既に存在する場合
            if(registUserRepository.isRegistedUser(requestRegistUser.getUserId()) > 0){
                throw new FailureRegistUserException("このユーザーIDは既に使用されています。");
            }
            int result = registUserRepository.registUser(requestRegistUser.getUserId(), requestRegistUser.getUserName(), requestRegistUser.getPassword());
            if (result != 1) {
                throw new FailureRegistUserException("ユーザー登録に失敗しました。");
            }
            String userId = requestRegistUser.getUserId();
            String userName = requestRegistUser.getUserName();
            boolean loginCheck = true;
            String message = "ユーザー登録に成功しました。";
            successResponseInfo.setCode("201");
            successResponseInfo.setMessage("success");
            responseSuccessRegistUser.setUserId(userId);
            responseSuccessRegistUser.setUserName(userName);
            responseSuccessRegistUser.setLoginCheck(loginCheck);
            responseSuccessRegistUser.setMessage(message);

            successRegistUser.setResponseInfo(successResponseInfo);
            successRegistUser.setData(responseSuccessRegistUser);

            // SuccessResponseInfoとresponseSuccessRegistUserを返す
            return successRegistUser;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ユーザーIDが重複しています。");
        } catch (Exception e) {
            throw new RuntimeException("サーバー内部でエラーが発生しました。");
        }
    }
}
