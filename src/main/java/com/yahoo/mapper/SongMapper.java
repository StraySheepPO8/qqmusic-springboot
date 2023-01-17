package com.yahoo.mapper;

import com.yahoo.pojo.entity.Song;
import com.yahoo.pojo.entity.UserLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SongMapper {
    List<Song> selectAll();
    Song selectById(Integer id);
    List<Song> selectByIds(List<UserLike> ids);
    List<Song> selectWithUsername(String username);

    int insert(Song song);
    int update(Map<String, Object> map);
    int delete(int id);
}
