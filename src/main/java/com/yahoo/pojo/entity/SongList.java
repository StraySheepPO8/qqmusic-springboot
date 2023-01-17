package com.yahoo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongList implements Serializable {
    public String type;
    public String pictureName;
    public String listName;
    public Double plays;
}
