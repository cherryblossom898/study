package com.example.coursequerysystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.coursequerysystem.entity.Student;
import com.example.coursequerysystem.entity.User;
import com.example.coursequerysystem.mapper.StudentMapper;
import com.example.coursequerysystem.mapper.UserMapper;
import com.example.coursequerysystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<Student> getStudentPage(Integer current, Integer size, String keyword) {
        Page<Student> page = new Page<>(current, size);
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Student::getStudentId, keyword)
                    .or()
                    .like(Student::getStudentName, keyword);
        }
        return this.page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStudent(Student student) {
        // 1. 唯一性校验
        boolean exists = lambdaQuery()
                .eq(Student::getStudentId, student.getStudentId())
                .exists();
        if (exists) {
            throw new RuntimeException("学生已存在，请重新输入");
        }
        // 2. 默认值处理
        if (!StringUtils.hasText(student.getEnrollmentYear())) {
            student.setEnrollmentYear(student.getStudentId().substring(0, 4));
        }
        if (!StringUtils.hasText(student.getGrade())) {
            student.setGrade(student.getEnrollmentYear() + "级");
        }
        // 3. 保存学生
        this.save(student);

        // 4. 同步创建 user 登录账号
        User user = new User();
        user.setUserId(generateUserId());
        user.setUsername(student.getStudentId());
        user.setPassword("123456");
        user.setRole("student");
        user.setRelateId(student.getStudentId());
        user.setStatus(1);
        userMapper.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStudent(String studentId) {
        // 1. 删除 user 表关联账号
        userMapper.delete(new LambdaQueryWrapper<User>().eq(User::getRelateId, studentId));
        // 2. 删除学生
        this.removeById(studentId);
    }

    private String generateUserId() {
        return "U" + System.currentTimeMillis();
    }
}