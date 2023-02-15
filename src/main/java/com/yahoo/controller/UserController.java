package com.yahoo.controller;

import com.sun.istack.internal.NotNull;
import com.yahoo.pojo.entity.User;
import com.yahoo.pojo.vo.UserVO;
import com.yahoo.service.inter.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Api(tags = "UserController用户管理")
public class UserController {

    @Autowired
    UserService userService;


    /**
     * 跳转登录页面
     */
    @GetMapping("/toLogin")
    @ApiOperation("请求登录页")
    public String toLogin() {
        return "user/login";
    }

    //    如果是@Controller，且方法返回值为void，
    //    则thymeleaf认为返回的视图名是接口（会拼接前后缀），找不到则报错


    /**
     * 登录
     *
     * @param user 用户信息
     * @param rem  记住我
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public String login(User user, String rem, Model model) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.username, user.password, true);
//        if (rem != null) {
//            token.setRememberMe(true);
//        }
        try {
            subject.login(token);
            return "redirect:/";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户不存在");
            return "user/login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误");
            return "user/login";
        }
    }


    /**
     * 查询用户信息
     */
    @GetMapping("/user/profile")
    @ApiOperation("查询用户信息")
    public String profile(Model model, HttpSession session) {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        UserVO userVo = userService.selectVoByUsername(loginUser.username);
        model.addAttribute("user", userVo);
        return "user/profile";
    }


    /**
     * 更新用户信息
     *
     * @param user 装载用户信息的entity
     */
    @PostMapping("/user/update")
    @ApiOperation("更新用户信息")
    public String update(User user, HttpSession session) {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.username;
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("user", user);
        try {
            System.out.println("---------------x");
            userService.updateByUsername(map);
            session.setAttribute("loginUser", userService.selectUserByUsername(user.username));
            return "redirect:/user/profile";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.setAttribute("msg", "&emsp;已存在用户名：" + user.username + "；请重新输入");
            return "redirect:/user/toEdit/" + username;
        }
    }


    /**
     * 跳转到编辑用户信息页面
     *
     * @param name notnull,restful风格的用户名参数
     */
    @GetMapping("/user/toEdit/{name}")
    @ApiOperation("请求修改用户信息的页面")
    public String edit(@PathVariable("name") @NotNull String name, Model model) {
        User user = userService.selectUserByUsername(name);
        model.addAttribute("user", user);
        return "user/editProfile";
    }


    /**
     * 跳转VIP页面
     */
    @GetMapping("/user/vip")
    @ApiOperation("请求VIP页面")
    public String vip() {
        return "user/VIP";
    }


    /**
     * 获取当前用户Principal
     * */
    @GetMapping("/getPrincipal")
    @ResponseBody
    public User getPrincipal(){
        System.out.println("=========getPrincipal");
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }


    /**
     * 退出登录
     */
    @GetMapping("/user/logout")
    @ApiOperation("退出登录")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/";
    }


    /**
     * 没有管理员认证时跳转的链接
     */
    @GetMapping("/user/noauthz")
    @ApiOperation("没有管理员认证时跳转的链接")
    @ResponseBody
    public String no() {
        return "需要管理员权限，您不是管理员。";
    }


}
