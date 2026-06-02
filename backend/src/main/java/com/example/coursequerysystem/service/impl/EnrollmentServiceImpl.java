package com.example.coursequerysystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.coursequerysystem.common.LoginUser;
import com.example.coursequerysystem.common.UserContext;
import com.example.coursequerysystem.entity.*;
import com.example.coursequerysystem.mapper.CourseMapper;
import com.example.coursequerysystem.mapper.EnrollmentMapper;
import com.example.coursequerysystem.mapper.TeachingMapper;
import com.example.coursequerysystem.mapper.TimeScheduleMapper;
import com.example.coursequerysystem.service.EnrollmentService;
import com.example.coursequerysystem.vo.MyCourseVO;
import com.example.coursequerysystem.vo.StudentScheduleVO;
import com.example.coursequerysystem.vo.TeachingStudentsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import  com.example.coursequerysystem.vo.AvailableCourseVO;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class EnrollmentServiceImpl extends ServiceImpl<EnrollmentMapper, Enrollment> implements EnrollmentService {

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Autowired
    private TeachingMapper teachingMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TimeScheduleMapper timeScheduleMapper;

    /**
     * 判断两个时间安排是否冲突
     * 冲突条件：同星期 + 节次有重叠
     */
    private boolean isTimeConflict(TimeSchedule ts1, TimeSchedule ts2) {
        // 星期不同，不冲突
        if (!ts1.getWeekday().equals(ts2.getWeekday())) {
            return false;
        }
        // 节次范围重叠判断（简化：直接比较字符串，因为你的节次格式如 "1-2节"）
        // 更严谨的做法是解析出开始节次和结束节次进行数值比较，这里先采用字符串方式演示
        // 实际应用中建议存储开始节次和结束节次数字
        return ts1.getSection().equals(ts2.getSection()); // 简化为相同节次才冲突
    }
    /**
     * 生成选课ID
     */

    private String generateEnrollmentId() {
        return "E" + System.currentTimeMillis(); // 简单生成
    }

    /**
     * 退课方法
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dropCourse(String studentId, String teachingId) {
        // 检查选课记录是否存在
        LambdaQueryWrapper<Enrollment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Enrollment::getStudentId, studentId)
                .eq(Enrollment::getTeachingId, teachingId);
        Enrollment enrollment = this.getOne(wrapper);
        if (enrollment == null) {
            throw new RuntimeException("你未选择该课程，无法退课");
        }

        // 删除选课记录
        this.remove(wrapper);

        // 更新课程已选人数 -1
        Teaching teaching = teachingMapper.selectById(teachingId);
        if (teaching != null) {
            LambdaUpdateWrapper<Course> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Course::getId, teaching.getCourseId())
                    .setSql("current_students = current_students - 1");
            courseMapper.update(null, updateWrapper);
        }
    }

    /**
     *查询教师授课学生名单
     */
    @Override
    public List<TeachingStudentsVO> getStudentsByTeachingId(String teachingId) {
        return enrollmentMapper.selectStudentsByTeachingId(teachingId);
    }



    @Override
    public List<StudentScheduleVO> getStudentSchedule(String studentId, String semester) {
        return enrollmentMapper.selectStudentSchedule(studentId, semester);
    }

    // 查询可选课程列表
    @Override
    public List<AvailableCourseVO> getAvailableCourses(String studentId, String semester) {
        return enrollmentMapper.selectAvailableCourses(studentId, semester);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)  // 保证数据一致性
    public void selectCourse(String studentId, String teachingId) {
        // ---------- 1. 获取教学班及课程信息 ----------
        Teaching teaching = teachingMapper.selectById(teachingId);
        if (teaching == null) {
            throw new RuntimeException("教学班不存在");
        }

        Course course = courseMapper.selectById(teaching.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        TimeSchedule timeSchedule = timeScheduleMapper.selectById(teaching.getScheduleId());
        if (timeSchedule == null) {
            throw new RuntimeException("上课时间不存在");
        }

        // ---------- 2. 重复选课校验 ----------
        int count = enrollmentMapper.countByStudentAndTeaching(studentId, teachingId);
        if (count > 0) {
            throw new RuntimeException("你已经选择过该课程，不能重复选课");
        }

        // ---------- 3. 容量校验 ----------
        if (course.getCurrentStudents() >= course.getMaxStudents()) {
            throw new RuntimeException("课程已满员，选课失败");
        }

        // ---------- 4. 时间冲突校验 ----------
        // 获取学生本学期所有已选课程的时间安排
        List<TimeSchedule> existingSchedules = enrollmentMapper.selectStudentSchedules(studentId, teaching.getSemester());
        for (TimeSchedule existing : existingSchedules) {
            if (isTimeConflict(timeSchedule, existing)) {
                throw new RuntimeException("与已选课程时间冲突，请重新选择");
            }
        }

        // ---------- 5. 保存选课记录 ----------
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(generateEnrollmentId()); // 自行生成ID，如 UUID 或时间戳
        enrollment.setStudentId(studentId);
        enrollment.setTeachingId(teachingId);
        enrollment.setCourseId(teaching.getCourseId());
        enrollment.setEnrollTime(LocalDateTime.now());
        this.save(enrollment);

        // ---------- 6. 更新课程已选人数 ----------
        LambdaUpdateWrapper<Course> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Course::getId, course.getId())
                .setSql("current_students = current_students + 1");
        courseMapper.update(null, updateWrapper);
    }


    @Override
    public List<MyCourseVO> getMyCourses(String studentId, String semester) {
        return enrollmentMapper.selectMyCourses(studentId, semester);
    }

    @Override
    public void updateGrade(String enrollmentId, Integer grade) {
        if (grade < 0 || grade > 100) {
            throw new RuntimeException("成绩必须在0-100之间");
        }
        Enrollment enrollment = this.getById(enrollmentId);
        if (enrollment == null) {
            throw new RuntimeException("选课记录不存在");
        }
        // 校验当前教师是否有权限（确保该教学班的教师是当前登录教师）
        LoginUser user = UserContext.get();
        Teaching teaching = teachingMapper.selectById(enrollment.getTeachingId());
        if (!teaching.getTeacherId().equals(user.getRelateId())) {
            throw new RuntimeException("无权修改此课程的成绩");
        }
        enrollment.setGrade(grade);
        this.updateById(enrollment);
    }

}