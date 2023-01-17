package com.yahoo.mapper;

import com.yahoo.pojo.entity.Song;
import com.yahoo.pojo.entity.UserLike;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserLikeMapper {
    List<UserLike> selectByUsername(String username);

    int delete(Map map);
}
