package com.yahoo.service.inter;

import com.yahoo.pojo.entity.User;
import com.yahoo.pojo.vo.UserVO;

import java.util.Map;

public interface UserService {
    UserVO selectVoByUsername(String username);
    User selectUserByUsername(String name);
    void updateByUsername(Map map);

}
