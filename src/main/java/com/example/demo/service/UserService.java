package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.List;

public interface UserService {

    User login(String userName, String password);

    User getUserById(Integer id);
}
