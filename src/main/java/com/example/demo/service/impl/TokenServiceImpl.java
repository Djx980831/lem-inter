package com.example.demo.service.impl;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.entity.User;
import com.example.demo.service.TokenService;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;

import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String getToken(User user) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 1000;  //一小时有效时间
        Date end = new Date(currentTime);
        String token = "";

        token = JWT.create().withAudience(user.getId()).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
