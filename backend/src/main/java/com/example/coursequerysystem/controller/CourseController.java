package com.example.coursequerysystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.coursequerysystem.common.AuthUtils;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.entity.Course;
import com.example.coursequerysystem.service.CourseService;
import com.example.coursequerysystem.vo.CoursePublicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 管理员分页查询课程
     */
    @GetMapping("/page")
    public Result<IPage<Course>> getCoursePage(@RequestParam(defaultValue = "1") Integer current,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               @RequestParam(required = false) String keyword) {
        AuthUtils.checkAdmin();
        IPage<Course> page = courseService.getCoursePage(current, size, keyword);
        return Result.success(page);
    }

    /**
     * 根据ID查询单个课程
     */
    // ========== 新增：管理端获取所有课程（下拉用） ==========
    @GetMapping("/all")
    public Result<List<Course>> getAllCourses() {
        return Result.success(courseService.list());
    }

    // ========== 原有的 /{id} 添加正则，仅匹配数字 ==========
    @GetMapping("/{id:\\d+}")
    public Result<Course> getCourseById(@PathVariable Integer id) {
        AuthUtils.checkAdmin();
        Course course = courseService.getById(id);
        return course != null ? Result.success(course) : Result.error("课程不存在");
    }

    /**
     * 新增课程
     */
    @PostMapping
    public Result<String> addCourse(@RequestBody Course course) {
        AuthUtils.checkAdmin();
        courseService.addCourse(course);
        return Result.success("新增成功");
    }

    /**
     * 修改课程
     */
    @PutMapping
    public Result<String> updateCourse(@RequestBody Course course) {
        AuthUtils.checkAdmin();
        courseService.updateCourse(course);
        return Result.success("修改成功");
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteCourse(@PathVariable Integer id) {
        AuthUtils.checkAdmin();
        courseService.deleteCourse(id);
        return Result.success("删除成功");
    }

    /**
     * 获取课程总数
     */
    @GetMapping("/count")
    public Result<Long> countCourses() {
        AuthUtils.checkAdmin();
        return Result.success(courseService.count());
    }

    /**
     * 热门课程（选课人数Top5）
     */
    @GetMapping("/popular")
    public Result<List<Course>> getPopularCourses() {
        List<Course> list = courseService.lambdaQuery()
                .orderByDesc(Course::getCurrentStudents)
                .last("LIMIT 5")
                .list();
        return Result.success(list);
    }

    /**
     * 获取所有课程（下拉用）
     */
    @GetMapping("/public/all")
    public Result<List<CoursePublicVO>> getPublicAllCourses() {
        List<CoursePublicVO> list = courseService.getPublicAllCourses();
        return Result.success(list);
    }


    /**
     * 公开课程查询（游客可访问）
     */
    @GetMapping("/public/page")
    public Result<IPage<CoursePublicVO>> getPublicCoursePage(@RequestParam(defaultValue = "1") Integer current,
                                                             @RequestParam(defaultValue = "10") Integer size,
                                                             @RequestParam(required = false) String courseName,
                                                             @RequestParam(required = false) String semester,
                                                             @RequestParam(required = false) String typeId) {
        IPage<CoursePublicVO> page = courseService.getPublicCoursePage(current, size, courseName, semester, typeId);
        return Result.success(page);
    }
}