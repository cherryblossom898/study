package com.example.coursequerysystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.coursequerysystem.entity.User;

public interface UserService extends IService<User> {

    /**
     * 用户登录验证
     * @param username 用户名
     * @param password 密码
     * @param role 角色
     * @return 验证成功返回 User 对象，否则返回 null
     */
    User login(String username, String password, String role);

    /**
     * 修改密码
     * @param userId
     * @param oldPassword
     * @param newPassword
     */
    void changePassword(String userId, String oldPassword, String newPassword);

}