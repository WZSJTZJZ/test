package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.dto.AdminDTO;
import com.example.springboot.controller.dto.AdminPasswordDTO;
import com.example.springboot.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

//IService 接口是 MyBatis Plus 提供的通用 Service 接口，扩展自 BaseService。使用 IService 接口可以获得一组常用的 CRUD 操作方法。
//        Service 接口需要继承 IService 接口，通过继承 IService 接口，实现类会自动获得基本的业务操作方法。
public interface IAdminService extends IService<Admin> {

    AdminDTO login(AdminDTO adminDTO);
    //接收用户登录数据
    Admin register(AdminDTO adminDTO);


    void updatePassword(AdminPasswordDTO adminPasswordDTO);

    Page<Admin> findPage(Page<Admin> objectPage, String username, Integer id);

    void batchInsert(List<Admin> admins);
}
