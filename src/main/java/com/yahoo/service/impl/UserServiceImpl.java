package com.yahoo.service.impl;

import com.yahoo.mapper.UserMapper;
import com.yahoo.pojo.dto.UserDTO;
import com.yahoo.pojo.entity.User;
import com.yahoo.pojo.vo.UserVO;
import com.yahoo.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserVO selectVoByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        return new UserDTO().toVO(user);
    }

    @Override
    public User selectUserByUsername(String name){
        return userMapper.selectByUsername(name);
    }

    @Override
    public void updateByUsername(Map map) {
        userMapper.updateByUsername(map);
    }








}
