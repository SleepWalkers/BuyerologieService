package com.buyerologie.user.dao;

import com.buyerologie.user.model.Profession;

public interface ProfessionDao {
    void deleteById(int id);

    void insert(Profession profession);

    Profession selectById(int id);

    void updateById(Profession profession);
}
