package com.yahoo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
//    private static final long serialVersionUID = 1L;
    public String username;
    public String password;
    public String permission;
    public String iconName;
    public String gender;
    public String phone;
}
