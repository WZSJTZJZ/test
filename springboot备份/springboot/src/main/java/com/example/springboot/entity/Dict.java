package com.example.springboot.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_dict")
@Data
public class Dict {
    public String name;
    public String value;
    public String type;
}
