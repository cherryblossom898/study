package com.example.coursequerysystem.controller;

import com.example.coursequerysystem.common.AuthUtils;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.service.EnrollmentService;
import com.example.coursequerysystem.vo.GradeUpdateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PutMapping("/grade")
    public Result<String> updateGrade(@RequestBody GradeUpdateVO vo) {
        AuthUtils.checkTeacher();
        enrollmentService.updateGrade(vo.getEnrollmentId(), vo.getGrade());
        return Result.success("成绩录入成功");
    }

    /**
     * 【管理员】获取选课总人次
     */
    @GetMapping("/count")
    public Result<Long> countEnrollments() {
        AuthUtils.checkAdmin();
        return Result.success(enrollmentService.count());
    }
}