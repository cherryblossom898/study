package com.example.coursequerysystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.coursequerysystem.entity.Enrollment;
import com.example.coursequerysystem.vo.AvailableCourseVO;
import com.example.coursequerysystem.vo.MyCourseVO;
import com.example.coursequerysystem.vo.StudentScheduleVO;
import com.example.coursequerysystem.vo.TeachingStudentsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnrollmentService extends IService<Enrollment> {

    /**
     * 获取学生个人课表
     */
    List<StudentScheduleVO> getStudentSchedule(String studentId, String semester);

    /**
     * 查询可选课程列表
     */
    List<AvailableCourseVO> getAvailableCourses(String studentId, String semester);

    /**
     * 学生选课（包含业务校验）
     * @param studentId  学号
     * @param teachingId 教学班ID
     * @throws RuntimeException 业务校验失败时抛出异常
     */
    void selectCourse(String studentId, String teachingId);

    /**
     * 学生退课
     */
    void dropCourse(String studentId, String teachingId);

    /**
     *查询教师授课学生名单
     */
    List<TeachingStudentsVO> getStudentsByTeachingId(String teachingId);

    /**
     * 查询教师教授的课程
     * @param studentId
     * @param semester
     * @return
     */
    List<MyCourseVO> getMyCourses(String studentId, String semester);

    /**
     * 教师录入成绩
     * @param enrollmentId
     * @param grade
     */
    void updateGrade(String enrollmentId, Integer grade);
}