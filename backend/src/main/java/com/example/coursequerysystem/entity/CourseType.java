package com.example.coursequerysystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("course_type")
public class CourseType {
    @TableId("type_id")
    private String typeId;

    @TableField("type_name")
    private String typeName;
}