package com.buyerologie.trade;

import java.util.List;

import com.buyerologie.trade.model.VipProduct;

public interface ProductService {

    List<VipProduct> getAllProducts();

    VipProduct get(int id);

    void add(VipProduct vipProduct);

    void edit(int productId, String name, double price, int availableDays, String description);

    void delete(int vipProductId);
}
