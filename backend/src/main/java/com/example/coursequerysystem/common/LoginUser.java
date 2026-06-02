package com.example.coursequerysystem.common;

import lombok.Data;

/**
 * 当前登录用户信息
 */
@Data
public class LoginUser {
    private String userId;      // user 表中的 user_id
    private String username;    // 登录账号
    private String role;        // admin / student / teacher
    private String relateId;    // 关联的学生学号 或 教师工号
    private String avatarUrl;  //用户头像url
}