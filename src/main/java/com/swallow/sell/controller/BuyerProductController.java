package com.swallow.sell.controller;

import com.swallow.sell.dataobject.ProductCategory;
import com.swallow.sell.dataobject.ProductInfo;
import com.swallow.sell.service.ICategoryService;
import com.swallow.sell.service.IProductService;
import com.swallow.sell.util.ResultVOUtil;
import com.swallow.sell.vo.ProductInfoVO;
import com.swallow.sell.vo.ProductVO;
import com.swallow.sell.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买家商品
 *
 * @author jdd
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;

    /**
     * 返回商品展示list
     * @return
     */
    @GetMapping(value = "/list")
    public ResultVO list(){
        //1.查询所有上架的商品
        List<ProductInfo> productInfoList = productService.findUpAll();
        //2.查询商品的类目
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);
        //3.数据拼接
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory :productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryType(productCategory.getCategoryType());
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtil.success(productVOList);
    }

}
