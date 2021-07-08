package com.example.demo.controller;

import com.example.demo.service.RedisService;
import com.example.demo.util.RpcResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version v1.0
 * @ProjectName: boot_token
 * @ClassName: RedisController
 * @Description: TODO(一句话描述该类的功能)
 * @Author: jingxiong.dong
 * @Date: 2021/7/8 13:52
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/setKey")
    public RpcResponse<String> setKey() {
        redisService.addCode("111", "222");
        return RpcResponse.success("success");
    }
}
