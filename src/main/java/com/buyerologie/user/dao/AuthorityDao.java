package com.buyerologie.user.dao;

import java.util.List;

import com.buyerologie.user.model.Authority;

public interface AuthorityDao {

    Authority selectById(int id);

    List<Authority> selectAll();
}