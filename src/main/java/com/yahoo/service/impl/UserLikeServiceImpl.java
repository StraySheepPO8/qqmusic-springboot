package com.yahoo.service.impl;

import com.yahoo.mapper.UserLikeMapper;
import com.yahoo.pojo.entity.UserLike;
import com.yahoo.service.UserLikeService;
import com.yahoo.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class UserLikeServiceImpl implements UserLikeService {
    @Autowired
    UserLikeMapper userLikeMapper;
    @Autowired
    RedisUtil redis;


    @Override
    public List<UserLike> selectByUsername(String username) {
        Object o = redis.get("user_like_" + username);
        if (o != null) return (List<UserLike>) o;

        List<UserLike> list = userLikeMapper.selectByUsername(username);

        redis.set("user_like_" + username, list);
        return list;
    }


    @Transactional
    @Override
    public int delete(Map map) {
        int delete = userLikeMapper.delete(map);

        if (delete == 0) return 0;
        String username = (String) map.get("username");
        redis.del("user_like_" + username);
        redis.del("song_user_like:" + username);
//        此处没必要set。set了只是增加代码量而已，没必要
        return delete;
    }


}
