package com.yahoo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yahoo.mapper.SongMapper;
import com.yahoo.pojo.dto.SongDTO;
import com.yahoo.pojo.entity.Song;
import com.yahoo.pojo.entity.UserLike;
import com.yahoo.pojo.vo.SongVO;
import com.yahoo.service.inter.SongService;
import com.yahoo.service.inter.UserLikeService;
import com.yahoo.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SongServiceImpl implements SongService {

    @Autowired
    SongMapper songMapper;
    @Autowired
    UserLikeService userLikeService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    RedisUtil redis;


    //     key的形式不是单纯的方法名，使用纯字符串也会报错
    @Override
    public List<SongVO> selectAll() {
        Object selectAll = redisTemplate.opsForValue().get("song_selectAll");
        if (selectAll != null) {
            return (List<SongVO>) selectAll;
        }

        SongDTO songDTO = new SongDTO();
        List<Song> songs = songMapper.selectAll();
        List<SongVO> vos = new ArrayList<>();
        for (Song song : songs) {
            vos.add(songDTO.toVO(song));
        }

        redisTemplate.opsForValue().set("song_selectAll", vos);
        return vos;
    }


    @Override
    public List<Song> selectSongPage(long currentPage, long pageSize) {
        Object o = redis.get("selectSongPage:" + currentPage + "," + pageSize);
        if (o != null) {
            return (List<Song>)o;
        }

//        查询包装类
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
//        设置查询条件：id字段的值大于12
//        queryWrapper.gt("id",12);

//        IPage是接口，Page是实现类。true代表查询总记录数，也有没有这个参数的重载方法
        Page<Song> page = new Page<>(currentPage, pageSize, true);

//        如果是单表查询，且mapper接口继承了BaseMapper<Song>就不需要写接口方法
        //   queryWrapper可以为null：不进行条件判断
//        返回List<Song>类型对象的方法selectPage
        IPage<Song> songPage = songMapper.selectPage(page, queryWrapper);
//        返回List<Map<String, Object>>类型对象的方法：selectMapsPage，其余代码一致
//        IPage<Map<String, Object>> mapIPage = studentMapper.selectMapsPage(page, wrapper);

        System.out.println("总页数:" + songPage.getPages());
        System.out.println("总记录数:" + songPage.getTotal());
        //getRecords()分页对象记录列表
        List<Song> records = songPage.getRecords();

        redis.set("selectSongPage:" + currentPage + "," + pageSize, records);
        return records;
    }


    //    使用springboot默认缓存时：key就为id
//    使用redis时：命名空间::key 为key
//    @Cacheable(key = "#id")
    @Override
    public Song selectById(Integer id) {
        Object o = redisTemplate.opsForValue().get("song_" + id);
        if (o != null) {
            return (Song) o;
        }

        Song song = songMapper.selectById(id);

        redisTemplate.opsForValue().set("song_" + id, song);
        return song;
    }


    @Override
    public List<Song> selectByUsername(String username) {
        /*
         * "song_user_like:" + username 返回 List<Song>
         * "user_like_" + username 返回 List<UserLike>
         * */
        Object o = redis.get("song_user_like:" + username);
        if (o != null) return (List<Song>) o;

        List<UserLike> userLikes = userLikeService.selectByUsername(username);
//        list类型使用new创建对象时，初始值就不是为null。list类型最好设置大小为零而不是null
        List<Song> songs = new ArrayList<>();
//        List<Song> songs =null;
        if (userLikes.size() != 0) {
            System.out.println(userLikes.toString());
            songs = songMapper.selectByIds(userLikes);
        }

        redis.set("song_user_like:" + username, songs);
        return songs;
    }


    //    此注解的作用是：存在两个及以上的代码功能模块时，当后一个执行失败时，前面那一个也不能成功执行，要回退
    @Transactional
    @Override
    public int insert(Song song) {
        int i = songMapper.insert(song);

//        不管redis是否存在这个key，都不会报错
        redis.del("song_selectAll");
        Integer id = song.id;
        if (id != null) redis.set("song_" + id, song);
        return i;
    }


    @Transactional
    @Override
    public int update(Map<String, Object> map) {
        int update = songMapper.update(map);
        /*
         * 更新可能出现主键冲突。若出现，则不会执行下面的代码。
         * 会执行catch代码块内的代码和try...catch代码块后面的代码。
         * */

        /*
         * 需要考虑的情况：
         * 1.全部查询
         * 2.id查询
         * 3.我的喜欢内容更新(*)
         * */

        /*
         * 该删除缓存的地方只能先del缓存，再set。不能自作聪明去直接set缓存。
         * 因为set的值没有删除，仍是在缓存中取得的。
         * 所以set值时要考虑set的值是不是在缓存中获取的
         * redis.set("song_selectAll", this.selectAll());
         * */

//        删除全部歌曲的缓存
        redis.del("song_selectAll");
//        更新单一歌曲。更新前后id可能不一样
        redis.del("song_" + map.get("id"));
        Song song = (Song) map.get("song");
        redis.set("song_" + song.id, song);
//        删除用户喜欢的song实体集合缓存
        Set<String> keys = redisTemplate.keys("song_user_like:*");
        assert keys != null;
        redisTemplate.delete(keys);
        return update;
    }


    @Transactional
    @Override
    public int delete(int id) {
        int delete = songMapper.delete(id);
        if (delete == 0) return 0;
//        执行下面代码的情况是：确实存在要删除的记录，且删除成功，不存在外键约束异常

        /*
         * 表之间存在外键约束：
         * 1.如果没有设置表的联级删除，问题就比较简单。
         *   抛出异常，无需考虑删除外键表的缓存
         * 2.如果设置了联级删除，需要删除外键表的缓存
         * */
        redis.del("song_selectAll");
        redis.del("song_" + id);
        return delete;
    }


}
