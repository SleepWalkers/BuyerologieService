package com.buyerologie.trade.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.buyerologie.trade.ProductService;
import com.buyerologie.trade.dao.VipProductDao;
import com.buyerologie.trade.model.VipProduct;

@Service("productService")
public class ProductServiceImp implements ProductService {

    @Resource
    private VipProductDao vipProductDao;

    @Override
    public List<VipProduct> getAllProducts() {
        return vipProductDao.selectAll();
    }

    @Override
    public VipProduct get(int id) {
        if (id <= 0) {
            return null;
        }
        return vipProductDao.selectById(id);
    }

}
