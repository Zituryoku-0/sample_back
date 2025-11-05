package com.example.sample_back.repository.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRepository {

    @Select("SELECT userid, username, userpassword FROM userinfo")
    UserRecord select();
}
