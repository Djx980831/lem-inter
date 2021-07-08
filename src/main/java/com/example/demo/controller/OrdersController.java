package com.example.demo.controller;

import com.example.demo.entity.Orders;
import com.example.demo.service.OrdersService;
import com.example.demo.util.RpcResponse;
import com.example.demo.vo.GoodsInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


/**
 * @version v1.0
 * @ProjectName: boot_token
 * @ClassName: OrdersController
 * @Author: jingxiong.dong
 * @Date: 2021/7/8 16:58
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/addOrders")
    public RpcResponse<String> addOrders(String email, Integer type, Integer amount, Integer faceValue, Integer price, Integer totalPrice, Integer onlyPay, Integer income) {
        Orders orders = new Orders();
        orders.setEmail(email);
        orders.setType(type);
        orders.setAmount(amount);
        orders.setFaceValue(faceValue);
        orders.setPrice(price);
        orders.setTotalPrice(totalPrice);
        orders.setOnlyPay(onlyPay);
        orders.setIncome(income);

        ordersService.addOrders(orders);
        return RpcResponse.success(email);
    }

    @PostMapping("/getGoodsInfo")
    public RpcResponse<GoodsInfoVO> getGoodsInfo(Integer type, Integer count) {
        return RpcResponse.success(ordersService.getGoodsInfo(type, count));
    }
}
