package com.example.coursequerysystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.coursequerysystem.entity.CourseType;

public interface CourseTypeService extends IService<CourseType> {
    IPage<CourseType> getCourseTypePage(Integer current, Integer size, String keyword);
}