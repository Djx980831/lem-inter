package com.example.demo.mapper;

import com.example.demo.entity.User;
import com.example.demo.vo.UserVO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    UserVO login(User user);

    String registUser(User user);

    UserVO getUserById(Integer id);

    UserVO getUserByEmail(String email);

    void updatePasswordByEmail(User user);
}
