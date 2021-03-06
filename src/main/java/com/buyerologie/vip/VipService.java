package com.buyerologie.vip;

import java.util.List;

import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.user.model.VipDetail;
import com.buyerologie.vip.exception.VipException;

public interface VipService {

    void add(int userId, int addedAvailableDays, long sourceOrder) throws TradeException,
                                                                  UserException, VipException;

    VipDetail getLastVipDetail(int userId);

    List<VipDetail> get(int page, int pageSize);

    int countVipNum();
}
