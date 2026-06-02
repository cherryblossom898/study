package com.example.coursequerysystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.coursequerysystem.entity.User;
import com.example.coursequerysystem.mapper.UserMapper;
import com.example.coursequerysystem.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(String username, String password, String role) {
        return lambdaQuery()
                .eq(User::getUsername, username)
                .eq(User::getPassword, password)
                .eq(User::getRole, role)
                .eq(User::getStatus, 1)    // 只允许状态为启用的账号登录
                .one();
    }

    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("原密码错误");
        }
        user.setPassword(newPassword);
        this.updateById(user);
    }

}