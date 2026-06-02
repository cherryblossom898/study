package com.example.coursequerysystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.coursequerysystem.entity.Teacher;
import com.example.coursequerysystem.vo.TeacherPublicVO;

import java.util.List;

public interface TeacherService extends IService<Teacher> {

    IPage<Teacher> getTeacherPage(Integer current, Integer size, String keyword);

    /**
     * 公开教师分页查询
     */
    IPage<TeacherPublicVO> getPublicTeacherPage(Integer current, Integer size,
                                                String teacherName, String departmentId, String title);

    List<TeacherPublicVO> getPublicAllTeachers();

    void addTeacher(Teacher teacher);

    void deleteTeacher(String teacherId);
}