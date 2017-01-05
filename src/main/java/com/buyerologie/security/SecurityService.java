package com.buyerologie.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.buyerologie.user.exception.UserException;

public interface SecurityService {

    public UserAuthCredentials getCurrentUser();

    public boolean isLogin();

    public void login(HttpServletRequest request, HttpServletResponse response, String username,
                      String password) throws UserException;

    public void logout(HttpServletRequest paramHttpServletRequest,
                       HttpServletResponse paramHttpServletResponse);

    public void setUserDetails(HttpServletRequest paramHttpServletRequest,
                               UserAuthCredentials paramUserAuthCredentials);

    public void reloadUserDetails();

    public void removeCache(int paramInt);

    public void removeCache(String paramString);

    public void removeCache(UserAuthCredentials paramUserAuthCredentials);

    public String getDefaultCache();

}
