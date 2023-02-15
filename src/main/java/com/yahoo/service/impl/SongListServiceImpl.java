package com.yahoo.service.impl;

import com.yahoo.mapper.SongListMapper;
import com.yahoo.pojo.entity.SongList;
import com.yahoo.service.inter.SongListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongListServiceImpl implements SongListService {
    @Autowired
    SongListMapper songListMapper;

    @Cacheable(value = "songList", key = "#a0")
    @Override
    public List<SongList> selectByRecommend(String type) {
        return songListMapper.selectByRecommend(type);
    }







}
