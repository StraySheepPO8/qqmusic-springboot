package com.yahoo.controller;

import com.yahoo.service.SongListService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Api(tags = "SongListController歌单管理")
public class SongListController {

    @Autowired
    SongListService songListService;

    //    /和/{type}需要分开写，因为形参type不能不赋值。除非为@Nullable


    /**
     * 查询歌单信息
     *
     * @param type 歌单类型
     */
    @GetMapping({"/", "/list/{type}"})
    public String songList(
            @PathVariable("type") @Nullable String type, Model model) {
        if (type == null) type = "recommendForYou";
//        else if (type.equals("favicon.ico")) return "index";
        System.out.println("=====================type:" + type);
        model.addAttribute("songList", songListService.selectByRecommend(type));
        model.addAttribute("type", type);
        model.addAttribute("link", "musicHouse");
        return "index";
    }


}
