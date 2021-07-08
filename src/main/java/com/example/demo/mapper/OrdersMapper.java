package com.example.demo.mapper;

import com.example.demo.entity.Orders;
import com.example.demo.entity.User;
import com.example.demo.vo.UserVO;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersMapper {
    void addOrders(Orders orders);

}
