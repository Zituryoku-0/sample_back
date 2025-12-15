package com.example.sample_back.repository.registUser;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RegistUserRepository {

     int registUser(@Param("userId") String userId, @Param("userName") String userName, @Param("password") String password);

     int isRegistedUser(@Param("userId") String userId);
}
