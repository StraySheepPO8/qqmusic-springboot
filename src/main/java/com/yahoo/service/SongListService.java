package com.yahoo.service;

import com.yahoo.pojo.entity.SongList;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SongListService {
    List<SongList> selectByRecommend(String type);
}
