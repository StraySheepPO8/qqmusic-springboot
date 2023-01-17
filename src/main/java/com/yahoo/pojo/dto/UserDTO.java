package com.yahoo.pojo.dto;

import com.yahoo.pojo.entity.User;
import com.yahoo.pojo.vo.UserVO;

public class UserDTO {
    public UserVO toVO(User user){
        UserVO vo = new UserVO();
        vo.username=user.username;
        vo.role=permsToRole(user.permission);
        vo.iconName=user.iconName;
        vo.gender=getGender(user.gender);
        vo.phone=user.phone;
        return vo;
    }

    public String getGender(String gender){
        if(gender.equals("男")){
            return "man";
        }else if (gender.equals("女")){
            return "woman";
        }else {
            return "";
        }
    }

    public String permsToRole(String perms){
        switch (perms) {
            case "common":
                return "普通用户";
            case "vip":
                return "VIP";
            case "admin":
                return "管理员";
            default:
                return "未知";
        }
    }





}
