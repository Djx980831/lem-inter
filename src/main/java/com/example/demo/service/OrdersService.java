package com.example.demo.service;

import com.example.demo.entity.Orders;
import com.example.demo.vo.GoodsInfoVO;

/**
 * @version v1.0
 * @ProjectName: boot_token
 * @ClassName: OrdersService
 * @Author: jingxiong.dong
 * @Date: 2021/7/8 16:57
 */
public interface OrdersService {
    void addOrders(Orders orders);

    GoodsInfoVO getGoodsInfo(String type, Integer count);
}
