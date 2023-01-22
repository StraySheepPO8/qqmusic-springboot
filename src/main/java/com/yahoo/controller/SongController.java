package com.yahoo.controller;

import com.yahoo.pojo.entity.Song;
import com.yahoo.pojo.entity.User;
import com.yahoo.service.SongService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "SongController歌曲管理")
public class SongController {

    @Autowired
    SongService songService;


    /**
     * 导航栏超链接：音乐馆，我喜欢的，全部歌曲，VIP
     * 根据参数link决定要返回的视图，且携带数据
     *
     * @param link restful风格的路径参数
     * @return String
     */
    @GetMapping("/song/{link}")
    @ApiOperation("查询全部歌曲")
    public String allMusic(
            @PathVariable("link") String link, Model model, HttpSession session) {
        switch (link) {
            case "allMusic":
                model.addAttribute("link", "allMusic");
//                model.addAttribute("allMusic", songService.selectAll());
                model.addAttribute("allMusic", songService.selectSongPage(1,5));
                return "song/allMusic";
            case "myMusic":
                User principal = (User) SecurityUtils.getSubject().getPrincipal();
                model.addAttribute("link", "myMusic");
                if (principal != null) {
                    if (!principal.permission.equals("admin")) {
                        System.out.println("loginUser对象不为null");
                        model.addAttribute("myMusic", songService.selectByUsername(principal.username));
                    }
                }else {
                    System.out.println("loginUser为null");
                }
                return "song/myMusic";
            case "vip":
                model.addAttribute("link", "vip");
                return "user/VIP";
            default:
                return "redirect:/";
        }
    }


    /**
     * 增加歌曲
     *
     * @param song 歌曲entity
     */
    @PostMapping("/song/crud/insert")
    @ApiOperation("添加歌曲记录")
    public String insert(Song song, Model model) {
        try {
            songService.insert(song);
            return "redirect:/song/allMusic";
        } catch (DuplicateKeyException e) {
            model.addAttribute("msg", "插入失败，存在歌曲的id为" + song.id);
            return "song/crud/addMusic";
        }
    }


    /**
     * 跳转到添加音乐页面
     */
    @GetMapping("/song/crud/toInsert")
    @ApiOperation("请求增加歌曲记录的页面")
    public String toInsert() {
        return "song/crud/addMusic";
    }


    /**
     * 删除歌曲记录。返回值为void，通过JavaWeb跳转视图
     *
     * @param id 歌曲主键id
     */
    @GetMapping("/song/crud/delete/{id}")
    @ResponseBody
    @ApiOperation("删除歌曲记录")
    public void delete(@PathVariable("id") int id, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try {
            int i = songService.delete(id);
            if (i == 0) {
                resp.getWriter().write("删除失败！不存在主键为" + id + "的歌曲记录");
            } else {
                resp.sendRedirect("/song/allMusic");
            }
        } catch (DataIntegrityViolationException e) {
            resp.getWriter().write("删除失败！外键冲突：所要删除的歌曲记录（id：" + id + "）被另一个表所依赖。");
        }

    }


    /**
     * get请求：跳转到修改歌曲信息页面
     * 携带原先歌曲的信息
     *
     * @param id 歌曲id
     */
    @GetMapping("/song/crud/update/{id}")
    @ApiOperation("请求更新歌曲信息的页面")
    public String update(@PathVariable("id") int id, Model model) {
        Song song = songService.selectById(id);
        model.addAttribute("song", song);
        return "song/crud/update";
    }


    /**
     * post请求：修改歌曲信息
     *
     * @param id   歌曲id
     * @param song 更改的信息的装载entity
     * @throws DuplicateKeyException 主键冲突
     */
    @PostMapping("/song/crud/update/{id}")
    @ApiOperation("更新歌曲信息")
    public String u(@PathVariable("id") int id, Song song, Model model) {
        Map<String, Object> map = new HashMap<>();
        map.put("song", song);
        map.put("id", id);
        try {
            songService.update(map);
            return "redirect:/song/allMusic";
        } catch (DuplicateKeyException e) {
            model.addAttribute("msg", "更该的歌曲的主键冲突，无法更改");
            return "song/crud/update";
        }
    }


    /**
     * 分页查询
     * @param currentPage 要查询的页数
     * @return List<Song> 响应json到前端
     * */
    @GetMapping("/song/moreSong")
    @ResponseBody
    public List<Song> moreSong(int currentPage){
        return songService.selectSongPage(currentPage,5);
    }

}
