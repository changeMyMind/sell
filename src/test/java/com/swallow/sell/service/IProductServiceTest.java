package com.swallow.sell.service;

import com.swallow.sell.dataobject.ProductInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author jdd
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IProductServiceTest {

    @Autowired
    private IProductService productService;

    @Test
    public void testFindByPage(){
        PageRequest request = new PageRequest(0,2);
        Page<ProductInfo> productInfos = productService.findAll(request);
        System.out.println(productInfos.getTotalElements());
    }

}