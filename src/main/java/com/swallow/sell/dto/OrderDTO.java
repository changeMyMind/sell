package com.swallow.sell.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.swallow.sell.dataobject.OrderDetail;
import com.swallow.sell.enums.OrderStatusEnum;
import com.swallow.sell.enums.PayStatusEnum;
import com.swallow.sell.util.serializer.Date2LongSerializer;
import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jdd
 */
@Data
public class OrderDTO {

    private String orderId;

    /**买家名字*/
    private String buyerName;

    /**买家电话*/
    private String buyerPhone;

    /**买家地址*/
    private String buyerAddress;

    /**买家微信openid*/
    private String buyerOpenid;

    /**订单总金额*/
    private BigDecimal orderAmount;

    /**'0''订单状态,默认为新下单'*/
    private Integer orderStatus;

    /**'0''支付状态,默认未支付'*/
    private Integer payStatus;

    /**产生订单时间*/
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /**更新时间*/
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    List<OrderDetail> orderDetailList;

}
