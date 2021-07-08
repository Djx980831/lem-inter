package com.example.demo.service.impl;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.entity.User;
import com.example.demo.service.TokenService;
import com.example.demo.vo.UserVO;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;

import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String getToken(UserVO user) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 1000;  //一小时有效时间
        Date end = new Date(currentTime);
        String token = "";

        token = JWT.create().withAudience(user.getId().toString()).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(user.getEmail()));
        return token;
    }
}
