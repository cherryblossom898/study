package com.example.coursequerysystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.coursequerysystem.entity.Department;
import com.example.coursequerysystem.mapper.DepartmentMapper;
import com.example.coursequerysystem.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public boolean save(Department department) {
        // 院系编号唯一性校验
        if (lambdaQuery().eq(Department::getDepartmentId, department.getDepartmentId()).exists()) {
            throw new RuntimeException("院系编号已存在，请重新输入");
        }
        return super.save(department);
    }

    @Override
    public IPage<Department> getDepartmentPage(Integer current, Integer size, String keyword) {
        Page<Department> page = new Page<>(current, size);
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Department::getDepartmentId, keyword)
                    .or()
                    .like(Department::getDepartmentName, keyword);
        }
        return this.page(page, wrapper);
    }


}