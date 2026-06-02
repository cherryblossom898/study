package com.example.coursequerysystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.coursequerysystem.entity.Teacher;
import com.example.coursequerysystem.entity.User;
import com.example.coursequerysystem.mapper.TeacherMapper;
import com.example.coursequerysystem.mapper.UserMapper;
import com.example.coursequerysystem.service.TeacherService;
import com.example.coursequerysystem.vo.TeacherPublicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<Teacher> getTeacherPage(Integer current, Integer size, String keyword) {
        Page<Teacher> page = new Page<>(current, size);
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Teacher::getTeacherId, keyword)
                    .or()
                    .like(Teacher::getTeacherName, keyword);
        }
        return this.page(page, wrapper);
    }

    @Override
    public List<TeacherPublicVO> getPublicAllTeachers() {
        return baseMapper.selectPublicAllTeachers();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTeacher(Teacher teacher) {
        boolean exists = lambdaQuery().eq(Teacher::getTeacherId, teacher.getTeacherId()).exists();
        if (exists) {
            throw new RuntimeException("教师已存在，请重新输入");
        }
        this.save(teacher);

        User user = new User();
        user.setUserId(generateUserId());
        user.setUsername(teacher.getTeacherId());
        user.setPassword("123456");
        user.setRole("teacher");
        user.setRelateId(teacher.getTeacherId());
        user.setStatus(1);
        userMapper.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTeacher(String teacherId) {
        userMapper.delete(new LambdaQueryWrapper<User>().eq(User::getRelateId, teacherId));
        this.removeById(teacherId);
    }

    @Override
    public IPage<TeacherPublicVO> getPublicTeacherPage(Integer current, Integer size,
                                                       String teacherName, String departmentId, String title) {
        Page<?> page = new Page<>(current, size);
        return baseMapper.selectPublicTeacherPage(page, teacherName, departmentId, title);
    }

    private String generateUserId() {
        return "U" + System.currentTimeMillis();
    }
}