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

    @Override
    public void add(VipProduct vipProduct) {
        if (vipProduct == null) {
            return;
        }
        vipProductDao.insert(vipProduct);
    }

    @Override
    public void delete(int vipProductId) {
        if (vipProductId <= 0) {
            return;
        }
        vipProductDao.deleteById(vipProductId);
    }

    @Override
    public void edit(int productId, String name, double price, int availableDays, String description) {
        if (productId <= 0) {
            return;
        }

        VipProduct originalVipProduct = vipProductDao.selectById(productId);
        if (originalVipProduct == null) {
            return;
        }
        originalVipProduct.setAvailableDays(availableDays);
        originalVipProduct.setDescription(description);
        originalVipProduct.setName(name);
        originalVipProduct.setPrice(price);
        vipProductDao.updateById(originalVipProduct);
    }

}
