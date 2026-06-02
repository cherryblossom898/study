package com.example.coursequerysystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.coursequerysystem.entity.CourseType;
import com.example.coursequerysystem.mapper.CourseTypeMapper;
import com.example.coursequerysystem.service.CourseTypeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeMapper, CourseType> implements CourseTypeService {

    @Override
    public boolean save(CourseType courseType) {
        // 课程类型编号唯一性校验
        if (lambdaQuery().eq(CourseType::getTypeId, courseType.getTypeId()).exists()) {
            throw new RuntimeException("课程类型编号已存在，请重新输入");
        }
        return super.save(courseType);
    }

    @Override
    public IPage<CourseType> getCourseTypePage(Integer current, Integer size, String keyword) {
        Page<CourseType> page = new Page<>(current, size);
        LambdaQueryWrapper<CourseType> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(CourseType::getTypeId, keyword)
                    .or()
                    .like(CourseType::getTypeName, keyword);
        }
        return this.page(page, wrapper);
    }
}