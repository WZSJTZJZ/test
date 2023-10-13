package com.example.springboot.controller;

import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/hello")
    public String Hello(){
        return "just so so !";
    }
//
//    @Autowired
//    private UserService userService;

    @GetMapping("/list1")
    public List<User> list(){
        return userService.list();
    }
    //新增
    @PostMapping("/save1")
    public boolean save(@RequestBody User user){
        return userService.save(user);
    }
    //修改
    @PostMapping("/mod1")
    public boolean mod(@RequestBody User user){
        return userService.updateById(user);
    }
    //新增或者修改
    @PostMapping("/saveormod1")
    public boolean saveormod1(@RequestBody User user){
        return userService.saveOrUpdate(user);
    }
    //删除
    @GetMapping("/delect1")
    public boolean delect(Integer id){
        return userService.removeById(id);
    }
}
