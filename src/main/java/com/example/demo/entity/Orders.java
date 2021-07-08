package com.example.demo.entity;

import lombok.Data;

/**
 * @version v1.0
 * @ProjectName: boot_token
 * @ClassName: Orders
 * @Author: jingxiong.dong
 * @Date: 2021/7/8 17:00
 */
@Data
public class Orders {
    private Integer id;
    private String email;
    private Integer type;
    private Integer amount;
    private Integer faceValue;
    private Integer price;
    private Integer totalPrice;
    private Integer onlyPay;
    private Integer income;
}
