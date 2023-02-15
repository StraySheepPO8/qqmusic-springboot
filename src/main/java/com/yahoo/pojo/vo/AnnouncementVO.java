package com.yahoo.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementVO implements Serializable{
    public String title;
    public String author;
    public String publishTime;
    public String content;
}
