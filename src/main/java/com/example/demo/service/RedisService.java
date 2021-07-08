package com.example.demo.service;

/**
 * @version v1.0
 * @ProjectName: boot_token
 * @ClassName: RedisService
 * @Description: TODO(一句话描述该类的功能)
 * @Author: jingxiong.dong
 * @Date: 2021/7/7 17:27
 */
public interface RedisService {
    Boolean addCode(String key, String value);

    String getCode(String key);

    Boolean hasKsy(String key);
}
