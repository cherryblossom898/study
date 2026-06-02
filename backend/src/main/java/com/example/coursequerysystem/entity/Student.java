package com.example.coursequerysystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("student")
public class Student {
    @TableId("student_id")
    private String studentId;

    @TableField("student_name")
    private String studentName;

    @TableField("gender")
    private String gender;

    @TableField("birth_date")
    private LocalDateTime birthDate;

    @TableField("enrollment_year")
    private String enrollmentYear;

    @TableField("grade")
    private String grade;

    @TableField("major")
    private String major;

    @TableField("class")
    private String clazz; // class是Java关键字，用clazz代替

    @TableField("student_phone")
    private String studentPhone;


    @TableField("department_id")
    private String departmentId;

    @TableField("avatar_url")
    private String avatarUrl; //用户头像

}