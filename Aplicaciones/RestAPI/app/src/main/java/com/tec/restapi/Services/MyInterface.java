package com.tec.restapi.Services;

public interface MyInterface {

    // for login
    void register();
    void login(String name, String email, String created_at);
    void logout();
}
