package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.vo.UserVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVO login(User user) {
        return userMapper.login(user);
    }

    @Override
    public String registUser(User user) {
        return userMapper.registUser(user);
    }

    @Override
    public UserVO getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public UserVO getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    public String updatePasswordByEmail(User user) {
        userMapper.updatePasswordByEmail(user);
        return user.getEmail();
    }
}
