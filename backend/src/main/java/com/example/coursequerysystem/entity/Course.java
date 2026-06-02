package com.example.coursequerysystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("course")
public class Course {

    @TableId(value = "id", type = IdType.AUTO)   // 主键是自增 id
    private Integer id;

    @TableField("course_code")
    private String courseCode;

    @TableField("course_name")
    private String courseName;

    @TableField("credit")
    private BigDecimal credit;

    @TableField("hours")
    private Integer hours;

    @TableField("type_id")
    private String typeId;

    @TableField("department_id")
    private String departmentId;

    @TableField("semester")
    private String semester;

    @TableField("max_students")
    private Integer maxStudents;

    @TableField("current_students")
    private Integer currentStudents;
}