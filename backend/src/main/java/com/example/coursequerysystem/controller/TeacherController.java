package com.example.coursequerysystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.coursequerysystem.common.AuthUtils;
import com.example.coursequerysystem.common.LoginUser;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.common.UserContext;
import com.example.coursequerysystem.entity.Teacher;
import com.example.coursequerysystem.service.EnrollmentService;
import com.example.coursequerysystem.service.TeacherService;
import com.example.coursequerysystem.vo.TeacherPublicVO;
import com.example.coursequerysystem.vo.TeachingStudentsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private EnrollmentService enrollmentService;

    /**
     * 【管理员】分页查询教师列表
     * @param current 当前页码（默认1）
     * @param size    每页条数（默认10）
     * @return 教师分页数据
     */
    @GetMapping("/page")
    public Result<IPage<Teacher>> getTeacherPage(@RequestParam(defaultValue = "1") Integer current,
                                                 @RequestParam(defaultValue = "10") Integer size,
                                                 @RequestParam(required = false) String keyword) {
        AuthUtils.checkAdmin();
        IPage<Teacher> page = teacherService.getTeacherPage(current, size, keyword);
        return Result.success(page);
    }

    /**
     * 【管理员】根据工号查询单个教师信息
     * @param teacherId 教师工号
     * @return 教师详细信息
     */
    @GetMapping("/{teacherId}")
    public Result<Teacher> getTeacherById(@PathVariable String teacherId) {
        AuthUtils.checkAdmin();
        Teacher teacher = teacherService.getById(teacherId);
        if (teacher == null) {
            return Result.error("教师不存在");
        }
        return Result.success(teacher);
    }

    @GetMapping("/all")
    public Result<List<Teacher>> getAllTeachers() {
        return Result.success(teacherService.list());
    }

    /**
     * 【管理员】获取教师总数
     */
    @GetMapping("/count")
    public Result<Long> countTeachers() {
        AuthUtils.checkAdmin();
        return Result.success(teacherService.count());
    }

    /**
     * 【管理员】新增教师
     * @param teacher 教师对象（JSON格式）
     * @return 操作结果
     */
    @PostMapping
    public Result<String> addTeacher(@RequestBody Teacher teacher) {
        AuthUtils.checkAdmin();
        teacherService.addTeacher(teacher);
        return Result.success("新增成功");
    }

    @PutMapping
    public Result<String> updateTeacher(@RequestBody Teacher teacher) {
        AuthUtils.checkAdmin();
        teacherService.updateById(teacher);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{teacherId}")
    public Result<String> deleteTeacher(@PathVariable String teacherId) {
        AuthUtils.checkAdmin();
        teacherService.deleteTeacher(teacherId);
        return Result.success("删除成功");
    }

    /**
     * 【教师】查看自己某教学班的学生名单
     * @param teachingId 教学班ID（注意：不是教师工号！）
     * @return 选课学生列表
     */
    @GetMapping("/teaching/{teachingId}/students")
    public Result<List<TeachingStudentsVO>> getTeachingStudents(@PathVariable String teachingId) {
        AuthUtils.checkTeacher();  // 确保是教师身份
        List<TeachingStudentsVO> list = enrollmentService.getStudentsByTeachingId(teachingId);
        return Result.success(list);
    }

    /**
     * 查看教师个人信息
     * @return
     */
    @GetMapping("/profile")
    public Result<Teacher> getTeacherProfile() {
        AuthUtils.checkTeacher();
        LoginUser user = UserContext.get();
        Teacher teacher = teacherService.getById(user.getRelateId());
        return Result.success(teacher);
    }


    /**
     * 公开教师查询（游客可访问）
     */
    /**
     * 公开教师查询（游客可访问，返回全部教师）
     */
    @GetMapping("/public/all")
    public Result<List<TeacherPublicVO>> getPublicAllTeachers() {
        List<TeacherPublicVO> list = teacherService.getPublicAllTeachers();
        return Result.success(list);
    }
}