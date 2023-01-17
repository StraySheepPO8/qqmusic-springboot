package com.yahoo.service;

import com.yahoo.pojo.entity.Song;
import com.yahoo.pojo.entity.UserLike;
import com.yahoo.pojo.vo.SongVO;

import java.util.List;
import java.util.Map;

public interface SongService {
    List<SongVO> selectAll();
    Song selectById(Integer id);
    List<Song> selectByUsername(String username);
//    List<Song> selectWithUsername(String username);

    int insert(Song song);

    int update(Map<String, Object> map);
    int delete(int id);
}
