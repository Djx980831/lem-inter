package com.example.demo.service.impl;

import com.example.demo.entity.Orders;
import com.example.demo.mapper.OrdersMapper;
import com.example.demo.service.OrdersService;
import com.example.demo.vo.GoodsInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @version v1.0
 * @ProjectName: boot_token
 * @ClassName: OrdersServiceImpl
 * @Author: jingxiong.dong
 * @Date: 2021/7/8 16:57
 */
@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    public static final HashMap<String, Integer> mobile = new HashMap<String, Integer>(){
        {
            put("10", 84);
            put("20", 82);
            put("30", 80);
            put("50", 78);
            put("100", 76);
        }
    };

    public static final HashMap<String, Integer> oil = new HashMap<String, Integer>(){
        {
            put("10", 95);
            put("20", 95);
            put("30", 95);
            put("50", 95);
            put("100", 95);
        }
    };

    @Override
    public void addOrders(Orders orders) {
        ordersMapper.addOrders(orders);
    }

    @Override
    public GoodsInfoVO getGoodsInfo(Integer type, Integer count) {
        GoodsInfoVO vo = new GoodsInfoVO();
        if (type == 1) {
           Integer price = mobile.get(count.toString());
           Integer totalPrice = 100 * count;
           Integer onlyPay = price * count;
           Integer income = totalPrice - onlyPay - 5;

           vo.setPrice(price);
           vo.setTotalPrice(totalPrice);
           vo.setOnlyPay(onlyPay);
           vo.setIncome(income);
           return vo;
        } else {
            Integer price = oil.get(count.toString());
            Integer totalPrice = 100 * count;
            Integer onlyPay = price * count;
            Integer income = totalPrice - onlyPay - 5;

            vo.setPrice(price);
            vo.setTotalPrice(totalPrice);
            vo.setOnlyPay(onlyPay);
            vo.setIncome(income);
            return vo;
        }
    }
}
