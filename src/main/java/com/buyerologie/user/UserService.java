package com.buyerologie.user;

import java.util.List;

import com.buyerologie.user.exception.UserException;
import com.buyerologie.user.model.User;

public interface UserService {
    /**
     * 若该用户名或邮件地址已被使用 返回true
     * @param nameOrEmail
     * @return
     */
    public boolean foundUser(String username);

    /**
     * 获取某个用户的信息
     * @param userId
     * @return
     */
    public User getUser(int userId);

    /**
     * 获取某个用户的信息
     * @param nameOrEmail
     * @return
     */
    public User getUser(String username);

    public User getCurrentUser();

    /**
     * 注册用户
     * @param registerInfo
     * @throws UserException 
     * @throws UserExistsException
     */
    public void register(User user) throws UserException;

    public void setPassword(int userId, String password) throws UserException;

    public void edit(User user) throws UserException;

    void modifyUserAuth(int userId, List<String> authList);

}
