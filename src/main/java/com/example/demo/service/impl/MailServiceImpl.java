package com.example.demo.service.impl;

import com.example.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: boot_token
 * @ClassName: MailServiceImpl
 * @Author: jingxiong.dong
 * @Date: 2021/7/7 19:44
 */
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sandSimpleMail(String to, String code) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("1530916212@qq.com");
        mailMessage.setTo(to);
        mailMessage.setSubject("邮件验证码");
        mailMessage.setText("您好，验证码为：" + code);
        javaMailSender.send(mailMessage);
    }
}
