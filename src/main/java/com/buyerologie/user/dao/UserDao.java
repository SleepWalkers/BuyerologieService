package com.buyerologie.user.dao;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.user.model.User;

public interface UserDao {
    void deleteById(int id);

    void insert(User user);

    User selectById(int id);

    void updateById(User user);

    User selectByUsername(@Param("username") String username);
}
