package com.example.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.dto.AdminPasswordDTO;
import com.example.springboot.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

//extends继承
//BaseMapper 接口是 MyBatis Plus 提供的通用 Mapper 接口，用于执行常用的 CRUD 操作，包括插入、更新、删除和查询等操作。
//        Mapper 接口需要继承 BaseMapper 接口，通过继承 BaseMapper 接口，实现类会自动获得基本的数据库操作方法。
public interface AdminMapper extends BaseMapper<Admin> {

    @Update("update admin set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(AdminPasswordDTO adminPasswordDTO);

    Page<Admin> findPage(Page<Admin> page, @Param("username") String username,@Param("id") Integer id);

    void batchInsert(@Param("admins") List<Admin> admins);
}
