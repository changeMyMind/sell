package com.swallow.sell.service.impl;

import com.swallow.sell.dataobject.ProductCategory;
import com.swallow.sell.repository.ProductCategoryRepository;
import com.swallow.sell.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jdd
 */
@Service
@Transactional
public class CategoryServiceimpl implements ICategoryService {

    @Autowired
    private ProductCategoryRepository repository;

    @Override
    public ProductCategory findOne(Integer id) {
        return repository.findOne(id);
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        repository.save(productCategory);
        return productCategory;
    }
}
