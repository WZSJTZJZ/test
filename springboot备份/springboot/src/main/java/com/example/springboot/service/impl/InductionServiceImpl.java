package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Induction;
import com.example.springboot.mapper.InductionMapper;
import com.example.springboot.service.IInductionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class InductionServiceImpl extends ServiceImpl<InductionMapper, Induction> implements IInductionService {

    @Resource
    private  InductionMapper inductionMapper;

    @Override
    public Page<Induction> findePage(Page<Induction> page, String username) {
        return inductionMapper.findPage(page,username);
    }
}
