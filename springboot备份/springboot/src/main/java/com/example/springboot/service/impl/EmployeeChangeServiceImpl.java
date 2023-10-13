package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.EmployeeChange;
import com.example.springboot.mapper.EmployeeChangeMapper;
import com.example.springboot.service.EmployeeChangeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeChangeServiceImpl extends ServiceImpl<EmployeeChangeMapper, EmployeeChange> implements EmployeeChangeService {
}