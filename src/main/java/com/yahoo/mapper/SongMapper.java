package com.yahoo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yahoo.pojo.entity.Song;
import com.yahoo.pojo.entity.UserLike;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SongMapper extends BaseMapper<Song> {
    List<Song> selectAll();
    Song selectById(Integer id);
    List<Song> selectByIds(List<UserLike> ids);
    List<Song> selectWithUsername(String username);

    int insert(Song song);
    int update(Map<String, Object> map);
    int delete(int id);

//        如果是单表查询，且mapper接口继承了BaseMapper<Song>
//        就不需要写接口方法selectPage(page, queryWrapper)或selectMapsPage(page, wrapper);

}
