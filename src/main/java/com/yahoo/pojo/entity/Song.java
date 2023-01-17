package com.yahoo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song implements Serializable {
    public Integer id;
    public String song;
    public String singer;
    public String album;
}
