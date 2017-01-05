package com.buyerologie.security;

public interface UserAuthCheck {
    public UserAuthCredentials selectById(int userId);

    public UserAuthCredentials selectByUserName(String username);
}