package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@TableName("employee_change")
public class EmployeeChange {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;                  // 记录ID
    @JsonProperty("adminId")
    private Integer employeeId;          // 员工工号
    @JsonProperty("admin")
    private String employeeName;         // 员工姓名
    @JsonProperty("modify")
    private String changeReason;         // 变更原因
    @TableField(value = "change_date", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date changeDate;             // 变更日期
    @TableField(value = "entry_date", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("entry")
    private Date entryDate;              // 入职日期
    @TableField(value = "regular_date", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("hireDate")
    private Date regularDate;            // 转正日期
    @JsonProperty("estate")
    private String jobLevel;             // 职级
    @JsonProperty("difference")
    private String employmentStatus;     // 就业状态
    @JsonProperty("site")
    private String workLocation;         // 工作地点
    @JsonProperty("department")
    private String department;           // 部门
    @TableField(value = "resignation_date", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("dimission")
    private Date resignationDate;        // 离职日期

}