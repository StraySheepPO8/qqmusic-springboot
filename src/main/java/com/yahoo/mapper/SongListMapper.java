package com.yahoo.mapper;

import com.yahoo.pojo.entity.SongList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SongListMapper {
    List<SongList> selectByRecommend(String type);

}
