package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.vo.UserVO;

import java.util.List;

public interface UserService {

    UserVO login(User user);

    String registUser(User user);

    UserVO getUserById(Integer id);

    UserVO getUserByEmail(String email);

    String updatePasswordByEmail(User user);
}
