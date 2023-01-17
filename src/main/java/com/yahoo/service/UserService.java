package com.yahoo.service;

import com.yahoo.pojo.entity.User;
import com.yahoo.pojo.vo.UserVO;

import java.util.Map;

public interface UserService {
    UserVO selectVoByUsername(String username);
    User selectUserByUsername(String name);
    int updateByUsername(Map map);

}
