package com.example.sample_back.repository.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {

    UserRecord selectUser(@Param("userId") String userId, @Param("password") String password);
}
