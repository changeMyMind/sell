package com.swallow.sell.service;

import com.swallow.sell.dataobject.OrderMaster;
import com.swallow.sell.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * 订单
 *
 * @author jdd
 */
public interface IOrderService {

    /**创建订单*/
    OrderDTO create(OrderDTO orderDTO);

    /**查询单个订单*/
    OrderDTO findOne(String orderId);

    /**查询订单列表*/
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

    /**取消订单2 完结订单1 支付订单3 */
    OrderDTO cancle(OrderDTO orderDTO,Integer status);

}
