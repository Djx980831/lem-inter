package com.example.demo.service;

/**
 * @version v1.0
 * @ProjectName: boot_token
 * @ClassName: MailService
 * @Description: TODO(一句话描述该类的功能)
 * @Author: jingxiong.dong
 * @Date: 2021/7/7 19:43
 */
public interface MailService {
    void sandSimpleMail(String to, String code);
}
