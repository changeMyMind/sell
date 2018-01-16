package com.swallow.sell.enums;

import lombok.Getter;

/**
 * 商品状态
 *
 * @author jdd
 */
@Getter
public enum ProductStatusEnum {
    UP(1, "上架"),
    DOWN(0, "下架")
    ;
    private Integer code;

    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
