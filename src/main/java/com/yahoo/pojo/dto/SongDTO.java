package com.yahoo.pojo.dto;

import com.yahoo.pojo.entity.Song;
import com.yahoo.pojo.vo.SongVO;

public class SongDTO {
    public SongVO toVO(Song s){
        SongVO vo = new SongVO();
        vo.id = s.id;
        vo.song = s.song;
        vo.singer = s.singer;
        vo.album = s.album;
        return vo;
    }


}
