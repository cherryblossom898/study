package com.example.coursequerysystem.common;

/**
 * 用户上下文工具类（基于 ThreadLocal）
 */
public class  UserContext {

    private static final ThreadLocal<LoginUser> CURRENT_USER = new ThreadLocal<>();

    /**
     * 设置当前登录用户
     */
    public static void set(LoginUser user) {
        CURRENT_USER.set(user);
    }

    /**
     * 获取当前登录用户
     */
    public static LoginUser get() {
        return CURRENT_USER.get();
    }

    /**
     * 清除当前线程的用户信息（请求结束后必须调用，防止内存泄漏）
     */
    public static void clear() {
        CURRENT_USER.remove();
    }
}