package com.buyerologie.vip.imp;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.buyerologie.trade.TradeService;
import com.buyerologie.trade.exception.OrderNotBelongToYouException;
import com.buyerologie.trade.exception.OrderNotExistException;
import com.buyerologie.trade.exception.TradeException;
import com.buyerologie.trade.model.TradeOrder;
import com.buyerologie.user.UserService;
import com.buyerologie.user.dao.VipDetailDao;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.user.exception.UserNotFoundException;
import com.buyerologie.user.model.User;
import com.buyerologie.user.model.VipDetail;
import com.buyerologie.utils.DateUtil;
import com.buyerologie.vip.VipService;
import com.buyerologie.vip.exception.DuplicateVipOrderException;
import com.buyerologie.vip.exception.VipException;

@Service("vipService")
public class VipServiceImp implements VipService {

    @Resource
    private VipDetailDao vipDetailDao;

    @Resource
    private UserService  userService;

    @Resource
    private TradeService alipayTradeService;

    @Override
    public void add(int userId, int addedAvailableDays,
                    long sourceOrder) throws TradeException, UserException, VipException {
        validate(userId, addedAvailableDays, sourceOrder);

        VipDetail vipDetail = vipDetailDao.selectLastByUserId(userId);

        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        Timestamp endTime = DateUtil.addDay(addedAvailableDays, startTime);

        if (vipDetail != null) {
            if (vipDetail.getEndTime().after(startTime)) {
                startTime = vipDetail.getStartTime();
                endTime = DateUtil.addDay(addedAvailableDays, vipDetail.getEndTime());
            }
        }
        vipDetail = new VipDetail();
        vipDetail.setSourceOrder(sourceOrder + "");
        vipDetail.setUserId(userId);
        vipDetail.setStartTime(startTime);
        vipDetail.setEndTime(endTime);
        vipDetailDao.insert(vipDetail);
    }

    private void validate(int userId, int addedAvailableDays,
                          long sourceOrder) throws TradeException, UserException, VipException {
        if (userId < 0) {
            throw new UserNotFoundException();
        }
        if (addedAvailableDays <= 0) {
            return;
        }
        if (sourceOrder < 0) {
            throw new OrderNotExistException();
        }

        User user = userService.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }

        TradeOrder tradeOrder = alipayTradeService.get(sourceOrder);
        if (tradeOrder == null) {
            throw new OrderNotExistException();
        }
        if (tradeOrder.getBuyerId() != userId) {
            throw new OrderNotBelongToYouException();
        }

        VipDetail vipDetail = vipDetailDao.selectBySourceOrder(sourceOrder + "");
        if (vipDetail != null) {
            throw new DuplicateVipOrderException();
        }

    }

    @Override
    public VipDetail getLastVipDetail(int userId) {
        if (userId < 0) {
            return null;
        }
        return vipDetailDao.selectLastByUserId(userId);
    }

}
