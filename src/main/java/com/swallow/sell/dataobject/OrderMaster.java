package com.swallow.sell.dataobject;

import com.swallow.sell.enums.OrderStatusEnum;
import com.swallow.sell.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单
 *
 * @author jdd
 */
@Entity
@DynamicUpdate
@Data
public class OrderMaster {

    @Id
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
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    /**'0''支付状态,默认未支付'*/
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    /**产生订单时间*/
    private Date createTime;

    /**更新时间*/
    private Date updateTime;

}

