package com.swallow.sell.service;

import com.swallow.sell.dataobject.ProductInfo;
import com.swallow.sell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 *
 * 商品
 *
 * @author jdd
 */
public interface IProductService {

    ProductInfo findOne(String productId);

    /**
     * 查询所有在架的商品
     * @return
     */
    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    //加库存
    void increaseStock(List<CartDTO> cartDTOList);

    //减库存
    void decreaseStock(List<CartDTO> cartDTOList);
}
