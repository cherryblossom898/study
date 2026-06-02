package com.example.coursequerysystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.coursequerysystem.entity.Student;

public interface StudentService extends IService<Student> {

    /**
     * 分页查询学生列表（支持关键词搜索）
     */
    IPage<Student> getStudentPage(Integer current, Integer size, String keyword);

    /**
     * 新增学生（含同步创建user账号）
     */
    void addStudent(Student student);

    /**
     * 删除学生（含同步删除user账号）
     */
    void deleteStudent(String studentId);
}