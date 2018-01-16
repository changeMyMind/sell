package com.swallow.sell.vo;

import lombok.Data;

/**
 * 前端交互 返回object
 *
 * @author jdd
 */
@Data
public class ResultVO<T> {

    /**编码*/
    private Integer code;
    /**编码表述*/
    private String msg;
    /**返回数据*/
    private T data;




}
