package com.yahoo.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class SongVO implements Serializable {
    public int id;
    public String song;
    public String singer;
    public String album;
}
