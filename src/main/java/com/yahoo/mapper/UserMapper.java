package com.yahoo.mapper;

import com.yahoo.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface UserMapper {
    User selectByUsername(String username);
    int updateByUsername(Map map);
}
