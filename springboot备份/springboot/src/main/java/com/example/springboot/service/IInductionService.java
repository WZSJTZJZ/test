package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Induction;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IInductionService extends IService<Induction> {

    Page<Induction> findePage(Page<Induction> page, String username);
}
