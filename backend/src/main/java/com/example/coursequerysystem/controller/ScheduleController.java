package com.example.coursequerysystem.controller;

import com.example.coursequerysystem.common.LoginUser;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.common.UserContext;
import com.example.coursequerysystem.service.EnrollmentService;
import com.example.coursequerysystem.service.TeachingService;
import com.example.coursequerysystem.vo.StudentScheduleVO;
import com.example.coursequerysystem.vo.TeacherScheduleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private EnrollmentService enrollmentService;

    /**
     * 学生查询个人课表（自动获取当前登录学生）
     * @param semester 学期（可选）
     */
    @GetMapping("/student")
    public Result<List<StudentScheduleVO>> getStudentSchedule(@RequestParam(required = false) String semester) {
        // 从上下文获取当前登录用户
        LoginUser user = UserContext.get();

        // 权限校验：只有学生角色可访问
        if (!"student".equals(user.getRole())) {
            return Result.error(403, "无权限访问，仅学生可查看个人课表");
        }

        // 使用 relateId（即学号）查询课表
        List<StudentScheduleVO> scheduleList = enrollmentService.getStudentSchedule(user.getRelateId(), semester);
        return Result.success(scheduleList);
    }

    @Autowired
    private TeachingService teachingService;

    /**
     * 教师查询个人授课安排
     */
    @GetMapping("/teacher")
    public Result<List<TeacherScheduleVO>> getTeacherSchedule(@RequestParam(required = false) String semester) {
        LoginUser user = UserContext.get();
        if (!"teacher".equals(user.getRole())) {
            return Result.error(403, "无权限访问，仅教师可查看授课安排");
        }
        List<TeacherScheduleVO> list = teachingService.getTeacherSchedule(user.getRelateId(), semester);
        return Result.success(list);
    }

}