package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.EmployeeChange;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeChangeMapper extends BaseMapper<EmployeeChange> {

}