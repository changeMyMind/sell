package com.swallow.sell.service.impl;

import com.swallow.sell.convert.OrderMaster2OrderDTOConverter;
import com.swallow.sell.dataobject.OrderDetail;
import com.swallow.sell.dataobject.OrderMaster;
import com.swallow.sell.dataobject.ProductInfo;
import com.swallow.sell.dto.CartDTO;
import com.swallow.sell.dto.OrderDTO;
import com.swallow.sell.enums.OrderStatusEnum;
import com.swallow.sell.enums.PayStatusEnum;
import com.swallow.sell.enums.ResultEnum;
import com.swallow.sell.exception.SellException;
import com.swallow.sell.repository.OrderDetailRepository;
import com.swallow.sell.repository.OrderMasterRepository;
import com.swallow.sell.service.IOrderService;
import com.swallow.sell.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.beans.Transient;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 *
 * @author jdd
 */
@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMasterRepository masterRepository;
    @Autowired
    private OrderDetailRepository detailRepository;
    @Autowired
    private ProductServiceImpl productService;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        String orderId = KeyUtil.getUniqueKey();
//        List<CartDTO> cartDTOList = new ArrayList<>();
        //查询商品 （数量价格）
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                //抛出异常处理机制
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //计算订单总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
            //写入订单数据库(orderMaster & orderDetail)
            //订单详情入库
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            detailRepository.save(orderDetail);
//            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(),orderDetail.getProductQuantity());
//            cartDTOList.add(cartDTO);
        }
        //订单入库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        orderDTO.setOrderAmount(orderAmount);
        orderDTO.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderDTO.setPayStatus(PayStatusEnum.WAIT.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        //扣库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(
                                            e -> new CartDTO(e.getProductId(),e.getProductQuantity())
                                    ).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = masterRepository.findOne(orderId);
        if(orderMaster!=null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = detailRepository.findByOrOrderId(orderMaster.getOrderId());
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> masterPage = masterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(masterPage.getContent());
        return new PageImpl<OrderDTO>(orderDTOList,pageable,masterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancle(OrderDTO orderDTO, Integer status) {
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        //取消订单
        if(OrderStatusEnum.CANCEL.getCode().equals(status)){
            //判断订单状态
            if(OrderStatusEnum.FINISHED.getCode().equals(orderDTO.getOrderStatus()) || OrderStatusEnum.CANCEL.getCode().equals(orderDTO.getOrderStatus())){
                log.error("【取消订单】订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
                throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
            }
            //修改订单状态
            orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
            OrderMaster updateMaster = masterRepository.save(orderMaster);
            if(updateMaster==null){
                log.error("【取消订单】更新失败，orderMaster={}",orderMaster);
                throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
            }
            //返回库存
            if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
                log.error("【取消订单】订单中无商品详情，orderDTO={}",orderDTO);
                throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
            }
            List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e->new CartDTO(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
            productService.increaseStock(cartDTOList);
            //如果支付 需要退款
            if(PayStatusEnum.SUCCESS.getCode().equals(orderDTO.getOrderStatus())){
                //TODO
            }
            return orderDTO;
        }

        if(OrderStatusEnum.FINISHED.getCode().equals(status)){
            //判断订单状态
            if(OrderStatusEnum.FINISHED.getCode().equals(orderDTO.getOrderStatus()) || OrderStatusEnum.CANCEL.getCode().equals(orderDTO.getOrderStatus())){
                log.error("【完结订单】订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
                throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
            }
            //修改订单状态
            orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
            OrderMaster master = new OrderMaster();
            BeanUtils.copyProperties(orderDTO,master);
            OrderMaster updateMaster = masterRepository.save(master);
            if(updateMaster==null){
                log.error("【完结订单】更新失败，orderMaster={}",orderMaster);
                throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
            }
            return orderDTO;
        }

        if(OrderStatusEnum.PAID.getCode().equals(status)){
            //判断订单状态
            if(OrderStatusEnum.FINISHED.getCode().equals(orderDTO.getOrderStatus()) || OrderStatusEnum.CANCEL.getCode().equals(orderDTO.getOrderStatus())){
                log.error("【订单支付完成订单】订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
                throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
            }
            //判断支付状态
            if(!PayStatusEnum.WAIT.getCode().equals(orderDTO.getPayStatus())){
                log.error("【订单支付完成订单】订单支付状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
                throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
            }
            //修改支付状态
            orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
            OrderMaster master = new OrderMaster();
            BeanUtils.copyProperties(orderDTO,master);
            OrderMaster updateMaster = masterRepository.save(master);
            if(updateMaster==null){
                log.error("【订单支付完成订单】更新失败，orderMaster={}",orderMaster);
                throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
            }
            return orderDTO;
        }

        return null;
    }
}
