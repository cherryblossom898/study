package com.example.coursequerysystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.coursequerysystem.entity.Department;

public interface DepartmentService extends IService<Department> {
    IPage<Department> getDepartmentPage(Integer current, Integer size, String keyword);
}