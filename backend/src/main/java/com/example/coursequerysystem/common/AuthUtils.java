package com.example.coursequerysystem.common;

/**
 * 权限校验工具类
 * 提供静态方法用于验证当前登录用户的角色
 */
public class AuthUtils {

    /**
     * 校验当前用户是否为管理员，否则抛出异常
     */
    public static void checkAdmin() {
        LoginUser user = UserContext.get();
        if (user == null) {
            throw new RuntimeException("未登录");
        }
        if (!"admin".equals(user.getRole())) {
            throw new RuntimeException("无权限访问");
        }
    }

    /**
     * 校验当前用户是否为学生
     */
    public static void checkStudent() {
        LoginUser user = UserContext.get();
        if (user == null) {
            throw new RuntimeException("未登录");
        }
        if (!"student".equals(user.getRole())) {
            throw new RuntimeException("无权限访问");
        }
    }

    /**
     * 校验当前用户是否为教师
     */
    public static void checkTeacher() {
        LoginUser user = UserContext.get();
        if (user == null) {
            throw new RuntimeException("未登录");
        }
        if (!"teacher".equals(user.getRole())) {
            throw new RuntimeException("无权限访问");
        }
    }

    /**
     * 获取当前登录用户（如果未登录则抛出异常）
     */
    public static LoginUser getCurrentUser() {
        LoginUser user = UserContext.get();
        if (user == null) {
            throw new RuntimeException("未登录");
        }
        return user;
    }
}