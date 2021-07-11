package com.example.demo.service;

import com.example.demo.vo.UserVO;

public interface TokenService {

    String getToken(UserVO userVO);
}
