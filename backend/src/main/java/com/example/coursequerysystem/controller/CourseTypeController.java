package com.example.coursequerysystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.coursequerysystem.common.AuthUtils;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.entity.CourseType;
import com.example.coursequerysystem.service.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course-type")
public class CourseTypeController {

    @Autowired
    private CourseTypeService courseTypeService;

    /**
     * 【管理员】分页查询课程类型列表
     */
    @GetMapping("/page")
    public Result<IPage<CourseType>> getCourseTypePage(@RequestParam(defaultValue = "1") Integer current,
                                                       @RequestParam(defaultValue = "10") Integer size,
                                                       @RequestParam(required = false) String keyword) {
        AuthUtils.checkAdmin();
        IPage<CourseType> page = courseTypeService.getCourseTypePage(current, size, keyword);
        return Result.success(page);
    }

    /**
     * 【管理员】新增课程类型
     */
    @PostMapping
    public Result<String> addCourseType(@RequestBody CourseType courseType) {
        AuthUtils.checkAdmin();
        courseTypeService.save(courseType);
        return Result.success("新增成功");
    }

    /**
     * 【管理员】修改课程类型
     */
    @PutMapping
    public Result<String> updateCourseType(@RequestBody CourseType courseType) {
        AuthUtils.checkAdmin();
        boolean updated = courseTypeService.updateById(courseType);
        return updated ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * 【管理员】删除课程类型
     */
    @DeleteMapping("/{typeId}")
    public Result<String> deleteCourseType(@PathVariable String typeId) {
        AuthUtils.checkAdmin();
        boolean removed = courseTypeService.removeById(typeId);
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 【公共】获取所有课程类型（用于下拉框）
     */
    @GetMapping("/all")
    public Result<List<CourseType>> getAllCourseTypes() {
        List<CourseType> list = courseTypeService.list();
        return Result.success(list);
    }
}