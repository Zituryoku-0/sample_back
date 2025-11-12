package com.example.sample_back.repository.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRepository {

    @Select("SELECT userID, userName FROM userinfo WHERE userId = #{userId} and password = #{password}")
    UserRecord select(@Param("userId") String userId, @Param("password") String password);
}
