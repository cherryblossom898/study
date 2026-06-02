package com.example.coursequerysystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.coursequerysystem.common.AuthUtils;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.entity.Department;
import com.example.coursequerysystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 【管理员】分页查询院系列表
     */
    @GetMapping("/page")
    public Result<IPage<Department>> getDepartmentPage(@RequestParam(defaultValue = "1") Integer current,
                                                       @RequestParam(defaultValue = "10") Integer size,
                                                       @RequestParam(required = false) String keyword) {
        AuthUtils.checkAdmin();
        IPage<Department> page = departmentService.getDepartmentPage(current, size, keyword);
        return Result.success(page);
    }

    /**
     * 【管理员】新增院系
     */
    @PostMapping
    public Result<String> addDepartment(@RequestBody Department department) {
        AuthUtils.checkAdmin();
        departmentService.save(department);
        return Result.success("新增成功");
    }

    /**
     * 【管理员】修改院系信息
     */
    @PutMapping
    public Result<String> updateDepartment(@RequestBody Department department) {
        AuthUtils.checkAdmin();
        boolean updated = departmentService.updateById(department);
        return updated ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * 【管理员】删除院系
     */
    @DeleteMapping("/{departmentId}")
    public Result<String> deleteDepartment(@PathVariable String departmentId) {
        AuthUtils.checkAdmin();
        boolean removed = departmentService.removeById(departmentId);
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 【公共】获取所有院系（用于下拉框）
     */
    @GetMapping("/all")
    public Result<List<Department>> getAllDepartments() {
        List<Department> list = departmentService.list();
        return Result.success(list);
    }
}