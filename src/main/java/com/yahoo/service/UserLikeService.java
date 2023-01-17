package com.yahoo.service;

import com.yahoo.pojo.entity.UserLike;

import java.util.List;
import java.util.Map;

public interface UserLikeService {
    List<UserLike> selectByUsername(String username);

    int delete(Map map);
}
