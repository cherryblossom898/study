package com.example.coursequerysystem.controller;

import com.example.coursequerysystem.common.AuthUtils;
import com.example.coursequerysystem.common.LoginUser;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.common.UserContext;
import com.example.coursequerysystem.entity.Student;
import com.example.coursequerysystem.entity.Teacher;
import com.example.coursequerysystem.service.StudentService;
import com.example.coursequerysystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${upload.path:uploads/avatars/}")
    private String uploadPath;

    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        LoginUser user = UserContext.get();
        // 确保已登录
        if (user == null) {
            return Result.error(401, "请先登录");
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String fileName = UUID.randomUUID().toString() + suffix;

        // 确保目录存在
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 保存文件
        File dest = new File(dir, fileName);
        file.transferTo(dest);

        // 生成访问URL（假设静态资源映射为 /uploads/**）
        String avatarUrl = "/uploads/avatars/" + fileName;

        // 更新对应用户的头像字段
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
            return Result.error("管理员暂不支持上传头像");
        }

        return Result.success("上传成功", avatarUrl);
    }
}