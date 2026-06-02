package com.example.coursequerysystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("department")
public class Department {
    @TableId("department_id")
    private String departmentId;

    @TableField("department_name")
    private String departmentName;

    @TableField("department_dean")
    private String departmentDean;

    @TableField("department_phone")
    private String departmentPhone;

    @TableField("department_addr")
    private String departmentAddr;
}