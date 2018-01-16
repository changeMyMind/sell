package com.swallow.sell.convert;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swallow.sell.dataobject.OrderDetail;
import com.swallow.sell.dto.OrderDTO;
import com.swallow.sell.enums.ResultEnum;
import com.swallow.sell.exception.SellException;
import com.swallow.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jdd
 */
@Slf4j
public class OrderForm2OrderDTOConverter {

    /**
     * orderForm 转换为 orderDTO
     * @param orderForm
     * @return
     */
    public static OrderDTO convert(OrderForm orderForm){
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(),new TypeToken<List<OrderDetail>>(){}.getType());
        } catch (Exception e){
            log.error("【对象转换错误】错误，string={}",orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

}
