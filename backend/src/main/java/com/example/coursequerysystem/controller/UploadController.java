package com.example.coursequerysystem.controller;

import com.example.coursequerysystem.common.AuthUtils;
import com.example.coursequerysystem.common.LoginUser;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.common.UserContext;
import com.example.coursequerysystem.config.UploadConfig;
import com.example.coursequerysystem.entity.Student;
import com.example.coursequerysystem.entity.Teacher;
import com.example.coursequerysystem.service.StudentService;
import com.example.coursequerysystem.service.TeacherService;
import com.example.coursequerysystem.common.FileUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    /**
     * 上传用户头像
     */
    @PostMapping("/avatar")
    public Result<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        // 确保用户已登录
        LoginUser user = UserContext.get();
        if (user == null) {
            return Result.error(401, "请先登录");
        }

        try {
            // 保存文件并获取访问URL
            String avatarUrl = FileUtils.saveAvatar(file, uploadConfig.getPath());

            // 根据角色更新对应表的 avatar_url 字段
            if ("student".equals(user.getRole())) {
                Student student = studentService.getById(user.getRelateId());
                if (student != null) {
                    student.setAvatarUrl(avatarUrl);
                    studentService.updateById(student);
                }
            } else if ("teacher".equals(user.getRole())) {
                Teacher teacher = teacherService.getById(user.getRelateId());
                if (teacher != null) {
                    teacher.setAvatarUrl(avatarUrl);
                    teacherService.updateById(teacher);
                }
            } else {
                // 管理员暂不支持头像（可根据需要扩展）
                return Result.error("管理员暂不支持上传头像");
            }

            Map<String, String> data = new HashMap<>();
            data.put("avatarUrl", avatarUrl);
            return Result.success("上传成功", data);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件保存失败: " + e.getMessage());
        }
    }

    /**
     * 访问上传的头像图片
     * @param filename 文件名
     * @param response HttpServletResponse
     */
    @GetMapping("/avatars/{filename}")
    public void getAvatar(@PathVariable String filename, HttpServletResponse response) throws IOException {
        File file = new File(uploadConfig.getPath(), filename);
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 设置内容类型（可根据扩展名动态判断，这里简化为通用图片类型）
        response.setContentType("image/png");
        // 将文件写入响应输出流
        Files.copy(file.toPath(), response.getOutputStream());
    }
}