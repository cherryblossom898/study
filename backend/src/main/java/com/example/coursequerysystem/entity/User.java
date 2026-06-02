package com.example.coursequerysystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)  // 或 IdType.INPUT 取决于你如何生成ID
    private String userId;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("role")
    private String role;              // admin/student/teacher

    @TableField("relate_id")
    private String relateId;          // 关联的学生学号或教师工号

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("status")
    private Integer status;           // 1启用 0禁用
}