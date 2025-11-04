package com.example.sample_back.repository.login;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    public UserRecord select(){
        return new UserRecord("sampleRepositoryUserId2", "sampleRepositoryUserName2");
    }
}
