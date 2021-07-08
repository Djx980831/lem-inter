package com.example.demo.vo;

import lombok.Data;

/**
 * @version v1.0
 * @ProjectName: boot_token
 * @ClassName: GoodsInfo
 * @Description: TODO(一句话描述该类的功能)
 * @Author: jingxiong.dong
 * @Date: 2021/7/8 18:02
 */
@Data
public class GoodsInfoVO {
    private Integer price;
    private Integer totalPrice;
    private Integer onlyPay;
    private Integer income;
}
