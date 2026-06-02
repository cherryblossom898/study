package com.example.coursequerysystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("teacher")
public class Teacher {
    @TableId("teacher_id")
    private String teacherId;

    @TableField("teacher_name")
    private String teacherName;

    @TableField("gender")
    private String gender;

    @TableField("title")
    private String title;

    @TableField("department_id")
    private String departmentId;

    @TableField("teacher_phone")
    private String teacherPhone;

    @TableField("hire_date")
    private LocalDateTime hireDate;

    @TableField("avatar_url")
    private String avatarUrl;
}