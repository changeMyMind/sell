package com.swallow.sell.repository;

import com.swallow.sell.dataobject.ProductInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author jdd
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository repository;

    @Test
    public void testSave(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("1234");
        productInfo.setProductName("皮蛋粥");
        productInfo.setProductPrice(BigDecimal.valueOf(22));
        productInfo.setProductStock(20);
        productInfo.setProductDescription("一个很好喝的早餐食品");
        productInfo.setProductIcon("http://www.baidu.com/1245.png");
        productInfo.setProductStatus(1);
        productInfo.setCategoryType(2);
        repository.save(productInfo);
    }

    @Test
    public void findByProductStatus(){
        List<ProductInfo> infoList = repository.findByProductStatus(1);
        System.out.println(infoList);
    }

}