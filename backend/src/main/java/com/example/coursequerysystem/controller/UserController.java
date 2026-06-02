package com.example.coursequerysystem.controller;

import com.example.coursequerysystem.common.LoginUser;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.common.UserContext;
import com.example.coursequerysystem.entity.Student;
import com.example.coursequerysystem.entity.Teacher;
import com.example.coursequerysystem.entity.User;
import com.example.coursequerysystem.interceptor.LoginInterceptor;
import com.example.coursequerysystem.service.StudentService;
import com.example.coursequerysystem.service.TeacherService;
import com.example.coursequerysystem.service.UserService;
import com.example.coursequerysystem.vo.ChangePasswordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.coursequerysystem.common.JwtUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用户登录
     * @param loginData
     * @return
     */

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        String role = loginData.get("role");

        if (username == null || password == null || role == null) {
            return Result.error("用户名、密码、角色不能为空");
        }

        User user = userService.login(username, password, role);
        if (user == null) {
            return Result.error("账号或密码错误，或账号已被禁用");
        }

        // 构建 LoginUser 对象
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setUsername(user.getUsername());
        loginUser.setRole(user.getRole());
        loginUser.setRelateId(user.getRelateId());

        // 生成 JWT Token
        String token = jwtUtils.generateToken(loginUser);

        // 获取真实姓名和头像（与学生/教师表关联）
        String realName = user.getUsername();
        String avatarUrl = null;
        if ("student".equals(role) && user.getRelateId() != null) {
            Student student = studentService.getById(user.getRelateId());
            if (student != null) {
                realName = student.getStudentName();
                avatarUrl = student.getAvatarUrl();
            }
        } else if ("teacher".equals(role) && user.getRelateId() != null) {
            Teacher teacher = teacherService.getById(user.getRelateId());
            if (teacher != null) {
                realName = teacher.getTeacherName();
                avatarUrl = teacher.getAvatarUrl();
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getUserId());
        data.put("username", user.getUsername());
        data.put("role", user.getRole());
        data.put("realName", realName);
        data.put("avatarUrl", avatarUrl);

        return Result.success("登录成功", data);
    }

    // 辅助方法：获取真实姓名
    private String getRealName(String role, String relateId) {
        if ("student".equals(role)) {
            Student s = studentService.getById(relateId);
            return s != null ? s.getStudentName() : "";
        } else if ("teacher".equals(role)) {
            Teacher t = teacherService.getById(relateId);
            return t != null ? t.getTeacherName() : "";
        }
        return "";
    }

    /**
     * 用户修改密码
     *
     */
    @PostMapping("/change-password")
    public Result<String> changePassword(@RequestBody ChangePasswordVO vo) {
        LoginUser user = UserContext.get();  // 必须登录
        userService.changePassword(user.getUserId(), vo.getOldPassword(), vo.getNewPassword());
        return Result.success("密码修改成功");
    }


}