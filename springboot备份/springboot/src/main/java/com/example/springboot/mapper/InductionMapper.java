package com.example.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Induction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface InductionMapper extends BaseMapper<Induction> {

    Page<Induction> findPage(Page<Induction> page, String username);
}
