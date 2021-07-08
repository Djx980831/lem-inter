package com.example.demo.service.impl;

import com.example.demo.service.RedisService;
import com.example.demo.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: boot_token
 * @ClassName: RedisServiceImpl
 * @Description: TODO(一句话描述该类的功能)
 * @Author: jingxiong.dong
 * @Date: 2021/7/7 17:27
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Boolean addCode(String key, String value) {
        return redisUtils.set(key, value, 600);
    }

    @Override
    public String getCode(String key) {
        return (String) redisUtils.get(key);
    }

    @Override
    public Boolean hasKsy(String key) {
        return redisUtils.hasKey(key);
    }
}
