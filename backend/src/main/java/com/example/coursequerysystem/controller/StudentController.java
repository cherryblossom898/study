package com.example.coursequerysystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.coursequerysystem.common.AuthUtils;
import com.example.coursequerysystem.common.LoginUser;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.common.UserContext;
import com.example.coursequerysystem.entity.Student;
import com.example.coursequerysystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;


    /**
     * 分页查询学生列表（仅管理员）
     */
    @GetMapping("/page")
    public Result<IPage<Student>> getStudentPage(@RequestParam(defaultValue = "1") Integer current,
                                                 @RequestParam(defaultValue = "10") Integer size,
                                                 @RequestParam(required = false) String keyword) {
        AuthUtils.checkAdmin();
        IPage<Student> page = studentService.getStudentPage(current, size, keyword);
        return Result.success(page);
    }

    /**
     * 根据学号查询单个学生（仅管理员）
     */
    @GetMapping("/{studentId}")
    public Result<Student> getStudentById(@PathVariable String studentId) {
        AuthUtils.checkAdmin();
        Student student = studentService.getById(studentId);
        if (student == null) {
            return Result.error("学生不存在");
        }
        return Result.success(student);
    }

    @PostMapping
    public Result<String> addStudent(@RequestBody Student student) {
        AuthUtils.checkAdmin();
        studentService.addStudent(student);
        return Result.success("新增成功");
    }

    @PutMapping
    public Result<String> updateStudent(@RequestBody Student student) {
        AuthUtils.checkAdmin();
        studentService.updateById(student);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{studentId}")
    public Result<String> deleteStudent(@PathVariable String studentId) {
        AuthUtils.checkAdmin();
        studentService.deleteStudent(studentId);
        return Result.success("删除成功");
    }

    /**
     * 【学生】获取当前登录学生的个人信息
     */
    @GetMapping("/profile")
    public Result<Student> getStudentProfile() {
        AuthUtils.checkStudent();
        LoginUser user = UserContext.get();
        Student student = studentService.getById(user.getRelateId());
        if (student == null) {
            return Result.error("学生信息不存在");
        }
        return Result.success(student);
    }

    /**
     * 【管理员】获取学生总数
     */
    @GetMapping("/count")
    public Result<Long> countStudents() {
        AuthUtils.checkAdmin();
        return Result.success(studentService.count());
    }

}