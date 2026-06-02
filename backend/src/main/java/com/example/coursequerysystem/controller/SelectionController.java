package com.example.coursequerysystem.controller;

import com.example.coursequerysystem.common.AuthUtils;
import com.example.coursequerysystem.common.LoginUser;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.common.UserContext;
import com.example.coursequerysystem.service.EnrollmentService;
import com.example.coursequerysystem.vo.AvailableCourseVO;
import com.example.coursequerysystem.vo.MyCourseVO;
import com.example.coursequerysystem.vo.SelectCourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/selection")
public class SelectionController {

    @Autowired
    private EnrollmentService enrollmentService;

    /**
     * 获取可选课程列表
     * @param semester 学期（可选，默认当前学期）
     */
    @GetMapping("/available")
    public Result<List<AvailableCourseVO>> getAvailableCourses(@RequestParam(required = false) String semester) {
        // 1. 校验学生身份
        AuthUtils.checkStudent();
        LoginUser user = UserContext.get();

        // 2. 如果没有传学期，可以设置默认学期（例如当前学期）
        if (semester == null || semester.isEmpty()) {
            semester = "2024-2025学年第一学期"; // 硬编码演示，实际可从系统配置读取
        }

        List<AvailableCourseVO> list = enrollmentService.getAvailableCourses(user.getRelateId(), semester);
        return Result.success(list);
    }

    /**
     * 查询学生已选课表
     * @param semester
     * @return
     */

    @GetMapping("/my")
    public Result<List<MyCourseVO>> getMyCourses(@RequestParam(required = false) String semester) {
        AuthUtils.checkStudent();
        LoginUser user = UserContext.get();
        List<MyCourseVO> list = enrollmentService.getMyCourses(user.getRelateId(), semester);
        return Result.success(list);
    }

    /**
     * 选课
     */
    @PostMapping("/select")
    public Result<String> selectCourse(@RequestBody SelectCourseVO vo) {
        // 1. 校验学生身份
        AuthUtils.checkStudent();
        LoginUser user = UserContext.get();

        // 2. 调用选课业务
        enrollmentService.selectCourse(user.getRelateId(), vo.getTeachingId());
        return Result.success("选课成功");
    }

    /**
     * 退课
     */
    @DeleteMapping("/drop")
    public Result<String> dropCourse(@RequestParam String teachingId) {
        // 1. 校验学生身份
        AuthUtils.checkStudent();
        LoginUser user = UserContext.get();

        // 2. 调用退课业务
        enrollmentService.dropCourse(user.getRelateId(), teachingId);
        return Result.success("退课成功");
    }
}