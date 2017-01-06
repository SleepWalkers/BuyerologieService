package com.buyerologie.trade;

import java.util.List;

import com.buyerologie.trade.model.VipProduct;

public interface ProductService {

    List<VipProduct> getAllProducts();

    VipProduct get(int id);
}
