package com.yahoo.controller;

import com.yahoo.pojo.entity.User;
import com.yahoo.service.inter.UserLikeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Api(tags = "UserLikeController用户收藏管理")
public class UserLikeController {

    @Autowired
    UserLikeService userLikeService;


    /**
     * 删除歌曲
     *
     * @param id 要删除的歌曲的id
     */
    @GetMapping("/userLike/delete/{id}")
    public String delete(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.username);
        map.put("id", id);
        int i = userLikeService.delete(map);
        if (i == 0) System.out.println("删除失败！");
        return "redirect:/song/myMusic";
    }

    @GetMapping("/setSession")
    @ResponseBody
    public void setSession(HttpSession session){
        session.setAttribute("user","lijialin");
    }
    @GetMapping("/getSession")
    @ResponseBody
    public void getSession(HttpSession session){
        User loginUser = (User) session.getAttribute("loginUser");
//        System.out.println("------------loginUser:"+loginUser.toString());
        System.out.println("------------user:"+session.getAttribute("user"));
    }


}
