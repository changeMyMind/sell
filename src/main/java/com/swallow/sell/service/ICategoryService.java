package com.swallow.sell.service;

import com.swallow.sell.dataobject.ProductCategory;

import java.util.List;

/**
 * @author jdd
 */
public interface ICategoryService {

    /**
     * 查询单个
     * @param id
     * @return
     */
    ProductCategory findOne(Integer id);

    /**
     * 查询所有
     * @return
     */
    List<ProductCategory> findAll();

    /**
     * 通过type查询
     * @param categoryTypeList
     * @return
     */
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    /**
     * 添加类目
     * @param productCategory
     */
    ProductCategory save(ProductCategory productCategory);

}
