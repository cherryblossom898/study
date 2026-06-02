package com.example.coursequerysystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.coursequerysystem.entity.Course;
import com.example.coursequerysystem.vo.CoursePublicVO;

import java.util.List;

public interface CourseService extends IService<Course> {

    /**
     * 分页查询课程（支持按课程代码或名称模糊搜索）
     */
    IPage<Course> getCoursePage(Integer current, Integer size, String keyword);

    /**
     * 公开课程分页查询（返回 CoursePublicVO）
     */
    IPage<CoursePublicVO> getPublicCoursePage(Integer current, Integer size,
                                              String courseName, String semester, String typeId);

    List<CoursePublicVO> getPublicAllCourses();
    /**
     * 新增课程（含联合唯一性校验）
     */
    void addCourse(Course course);

    /**
     * 修改课程
     */
    void updateCourse(Course course);

    /**
     * 删除课程（根据主键ID）
     */
    void deleteCourse(Integer id);


}