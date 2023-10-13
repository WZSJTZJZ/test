package com.example.springboot.controller.dto;

import com.example.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
public class AdminDTO {
    private String username;
    private String password;
    private String nick;
    private String avatar;
    private String token;
    private String role;
    private List<Menu> menus ;
}