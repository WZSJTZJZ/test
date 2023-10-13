package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@TableName("admin")

@ToString
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String sex;
    private String place;
    private String school;
    private String speciality;
    private String education;
    private String birthday;
    private String phone;
    private LocalDateTime creatTime;
    private String role;
    @TableField(exist = false)
    private List<Induction> inductions;
}
