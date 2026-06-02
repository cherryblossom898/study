package com.example.coursequerysystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.coursequerysystem.entity.Enrollment;
import com.example.coursequerysystem.entity.TimeSchedule;
import com.example.coursequerysystem.vo.AvailableCourseVO;
import com.example.coursequerysystem.vo.MyCourseVO;
import com.example.coursequerysystem.vo.StudentScheduleVO;
import com.example.coursequerysystem.vo.TeachingStudentsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnrollmentMapper extends BaseMapper<Enrollment> {

    /**
     * 查询学生个人课表
     * @param studentId 学号
     * @param semester 学期（可选，传 null 则查询所有学期）
     */
    List<StudentScheduleVO> selectStudentSchedule(@Param("studentId") String studentId,
                                                  @Param("semester") String semester);


    /**
     * 查询可选课程列表（带是否已选标记）
     * @param studentId 学生学号
     * @param semester  学期
     */
    List<AvailableCourseVO> selectAvailableCourses(@Param("studentId") String studentId,
                                                   @Param("semester") String semester);

    /**
     * 检查学生是否已选某个教学班
     */
    int countByStudentAndTeaching(@Param("studentId") String studentId,
                                  @Param("teachingId") String teachingId);

    /**
     * 查询学生已选课程的时间安排（用于冲突检测）
     */
    List<TimeSchedule> selectStudentSchedules(@Param("studentId") String studentId,
                                              @Param("semester") String semester);

    /**
     *查询教师授课学生名单
     */
    List<TeachingStudentsVO> selectStudentsByTeachingId(@Param("teachingId") String teachingId);

    /**
     * 查询学生已选课程列表
     * @param studentId 学号
     * @param semester  学期（可选）
     */
    List<MyCourseVO> selectMyCourses(@Param("studentId") String studentId,
                                     @Param("semester") String semester);
}