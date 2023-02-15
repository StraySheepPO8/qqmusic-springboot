package com.yahoo.controller;

import com.yahoo.pojo.vo.AnnouncementVO;
import com.yahoo.service.inter.AnnouncementService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags = "消息控制类")
public class MsgController {

    @Autowired
    AnnouncementService announcementService;

    @GetMapping("/msg/toPublish")
    public String to(){
        return "msg/publish";
    }

    @PostMapping("/msg/publish")
    @ResponseBody
    public String publish(AnnouncementVO vo){
        announcementService.sendMsg(vo);
        System.out.println(vo.toString());
        return "发布成功，<a href='/'>回首页</a>";
    }

    @GetMapping("/msg/getMsg")
    public String getMsg(Model model) {
        AnnouncementVO message = announcementService.getMsg();
        model.addAttribute("msg", message);
        return "msg/msg";
    }

}
