package com.example.coursequerysystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.coursequerysystem.entity.Course;
import com.example.coursequerysystem.mapper.CourseMapper;
import com.example.coursequerysystem.service.CourseService;
import com.example.coursequerysystem.vo.CoursePublicVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Override
    public IPage<Course> getCoursePage(Integer current, Integer size, String keyword) {
        Page<Course> page = new Page<>(current, size);
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Course::getCourseCode, keyword)
                    .or()
                    .like(Course::getCourseName, keyword);
        }
        return this.page(page, wrapper);
    }

    @Override
    public IPage<CoursePublicVO> getPublicCoursePage(Integer current, Integer size, String courseName, String semester, String typeId) {
        Page<?> page = new Page<>(current, size);
        return baseMapper.selectPublicCoursePage(page, courseName, semester, typeId);
    }

    @Override
    public List<CoursePublicVO> getPublicAllCourses() {
        return baseMapper.selectPublicAllCourses();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCourse(Course course) {
        // 基本字段校验
        if (!StringUtils.hasText(course.getCourseCode())) {
            throw new RuntimeException("课程代码不能为空");
        }
        if (!StringUtils.hasText(course.getCourseName())) {
            throw new RuntimeException("课程名称不能为空");
        }
        if (course.getCredit() == null || course.getCredit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("学分必须大于0");
        }
        if (course.getHours() == null || course.getHours() <= 0) {
            throw new RuntimeException("学时必须大于0");
        }
        if (!StringUtils.hasText(course.getSemester())) {
            throw new RuntimeException("学期不能为空");
        }

        // 联合唯一性校验：课程代码 + 学期
        boolean exists = lambdaQuery()
                .eq(Course::getCourseCode, course.getCourseCode())
                .eq(Course::getSemester, course.getSemester())
                .exists();
        if (exists) {
            throw new RuntimeException("该课程代码在当前学期已存在，请重新输入");
        }

        // 初始选课人数为0
        course.setCurrentStudents(0);
        this.save(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCourse(Course course) {
        if (course.getId() == null) {
            throw new RuntimeException("课程ID不能为空");
        }
        Course existing = this.getById(course.getId());
        if (existing == null) {
            throw new RuntimeException("课程不存在");
        }

        // 如果修改了课程代码或学期，需校验联合唯一性
        if ((course.getCourseCode() != null && !course.getCourseCode().equals(existing.getCourseCode())) ||
                (course.getSemester() != null && !course.getSemester().equals(existing.getSemester()))) {

            String newCode = course.getCourseCode() != null ? course.getCourseCode() : existing.getCourseCode();
            String newSemester = course.getSemester() != null ? course.getSemester() : existing.getSemester();

            boolean exists = lambdaQuery()
                    .eq(Course::getCourseCode, newCode)
                    .eq(Course::getSemester, newSemester)
                    .ne(Course::getId, course.getId())
                    .exists();
            if (exists) {
                throw new RuntimeException("该课程代码在当前学期已存在，请重新输入");
            }
        }

        this.updateById(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCourse(Integer id) {
        // 检查是否存在关联的排课记录（teaching表）
        // 实际项目中应注入 TeachingMapper 检查，此处简化为直接删除
        // 若存在外键约束且未设置级联删除，将抛出异常由全局异常处理器处理
        this.removeById(id);
    }


}