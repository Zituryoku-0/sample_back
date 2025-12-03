// java
package com.example.sample_back.service.user;

import com.example.sample_back.repository.login.UserRecord;
import com.example.sample_back.repository.login.UserRepository;
import com.example.sample_back.service.login.LoginService;
import com.example.sampleback.model.RequestLogin;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private RequestLogin request;

    @Mock
    private UserRecord userRecord;

    @InjectMocks
    private LoginService loginService;

    private Object getFieldValue(Object obj, String fieldName) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(obj);
        } catch (NoSuchFieldException nsfe) {
            // try getter style fallback
            try {
                String capital = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    return obj.getClass().getMethod("get" + capital).invoke(obj);
                } catch (NoSuchMethodException e) {
                    return obj.getClass().getMethod("is" + capital).invoke(obj);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void find_whenUserExists_returnsTrimmedUserEntity() {
        when(request.getUserId()).thenReturn("  uid  ");
        when(request.getPassword()).thenReturn(" pass ");
        when(repository.selectUser("  uid  ", " pass ")).thenReturn(userRecord);
        when(userRecord.getUserId()).thenReturn(" uid ");
        when(userRecord.getUserName()).thenReturn(" name ");

        Object result = loginService.find(request);

        assertEquals("uid", getFieldValue(result, "userId"));
        assertEquals("name", getFieldValue(result, "userName"));
        assertEquals(Boolean.TRUE, getFieldValue(result, "loginCheck"));

        verify(repository, times(1)).selectUser("  uid  ", " pass ");
    }

    @Test
    void find_whenUserNotFound_returnsEmptyUserEntity() {
        when(request.getUserId()).thenReturn("nope");
        when(request.getPassword()).thenReturn("nopass");
        when(repository.selectUser("nope", "nopass")).thenReturn(null);

        Object result = loginService.find(request);

        assertEquals("", getFieldValue(result, "userId"));
        assertEquals("", getFieldValue(result, "userName"));
        assertEquals(Boolean.FALSE, getFieldValue(result, "loginCheck"));

        verify(repository, times(1)).selectUser("nope", "nopass");
    }

    @Test
    void find_whenTooManyResults_throwsIllegalArgumentException() {
        when(request.getUserId()).thenReturn("dup");
        when(request.getPassword()).thenReturn("p");
        when(repository.selectUser("dup", "p")).thenThrow(new TooManyResultsException("too many"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> loginService.find(request));
        assertTrue(ex.getMessage().contains("複数のユーザー"));
        verify(repository, times(1)).selectUser("dup", "p");
    }

    @Test
    void find_whenOtherException_throwsRuntimeException() {
        when(request.getUserId()).thenReturn("err");
        when(request.getPassword()).thenReturn("p");
        when(repository.selectUser("err", "p")).thenThrow(new RuntimeException("db error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> loginService.find(request));
        assertTrue(ex.getMessage().contains("何かしらの例外"));
        verify(repository, times(1)).selectUser("err", "p");
    }
}