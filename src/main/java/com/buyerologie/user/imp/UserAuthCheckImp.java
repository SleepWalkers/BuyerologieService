package com.buyerologie.user.imp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.buyerologie.security.UserAuthCheck;
import com.buyerologie.security.UserAuthCredentials;
import com.buyerologie.user.UserService;

@Service("userAuthCheck")
public class UserAuthCheckImp implements UserAuthCheck {

    @Resource
    private UserService userService;

    @Override
    public UserAuthCredentials selectById(int userId) {
        return userService.getUser(userId);
    }

    @Override
    public UserAuthCredentials selectByUserName(String username) {
        return userService.getUser(username);
    }

}
