package com.example.sample_back.repository.registUser;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RegistUserRepository {

     int registUser(@Param("userid") String userId, @Param("username") String userName, @Param("password") String password);
}
