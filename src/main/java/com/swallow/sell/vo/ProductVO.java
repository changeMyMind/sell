package com.swallow.sell.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swallow.sell.dataobject.ProductInfo;
import lombok.Data;

import java.util.List;

/**
 * 商品
 *
 * @author jdd
 */
@Data
public class ProductVO {

    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;

}
