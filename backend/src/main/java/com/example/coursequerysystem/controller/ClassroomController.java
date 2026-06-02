package com.example.coursequerysystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.coursequerysystem.common.AuthUtils;
import com.example.coursequerysystem.common.LoginUser;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.common.UserContext;
import com.example.coursequerysystem.entity.Classroom;
import com.example.coursequerysystem.service.ClassroomService;
import com.example.coursequerysystem.vo.ClassroomAvailableQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classroom")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;


    // 分页查询（带条件）
    @GetMapping("/page")
    public Result<IPage<Classroom>> getClassroomPage(@RequestParam(defaultValue = "1") Integer current,
                                                     @RequestParam(defaultValue = "10") Integer size,
                                                     @RequestParam(required = false) String keyword) {
        AuthUtils.checkAdmin();
        IPage<Classroom> page = classroomService.getClassroomPage(current, size, keyword);
        return Result.success(page);
    }

    // 根据 ID 查询
    @GetMapping("/{roomId}")
    public Result<Classroom> getClassroomById(@PathVariable String roomId) {
        AuthUtils.checkAdmin();
        Classroom classroom = classroomService.getById(roomId);  // ✅ 修改这里
        return classroom != null ? Result.success(classroom) : Result.error("教室不存在");
    }

    @GetMapping("/all")
    public Result<List<Classroom>> getAllClassrooms() {
        return Result.success(classroomService.list());
    }

    /**
     * 【管理员】获取教室总数
     */
    @GetMapping("/count")
    public Result<Long> countClassrooms() {
        AuthUtils.checkAdmin();
        return Result.success(classroomService.count());
    }

    // 新增
    @PostMapping
    public Result<String> addClassroom(@RequestBody Classroom classroom) {
        AuthUtils.checkAdmin();
        classroomService.addClassroom(classroom);
        return Result.success("新增成功");
    }

    // 修改
    @PutMapping
    public Result<String> updateClassroom(@RequestBody Classroom classroom) {
        AuthUtils.checkAdmin();
        boolean updated = classroomService.updateById(classroom);  // ✅ 修改这里
        return updated ? Result.success("修改成功") : Result.error("修改失败");
    }

    // 删除
    @DeleteMapping("/{roomId}")
    public Result<String> deleteClassroom(@PathVariable String roomId) {
        AuthUtils.checkAdmin();
        boolean removed = classroomService.removeById(roomId);  // ✅ 修改这里
        return removed ? Result.success("删除成功") : Result.error("删除失败");
    }


    /**
     * 【公共查询】查询空闲教室
     * @param vo 查询条件（学期、星期、节次）
     * @return 空闲教室列表
     */
    @PostMapping("/public/available")
    public Result<List<Classroom>> getAvailableClassrooms(@RequestBody ClassroomAvailableQueryVO vo) {
        List<Classroom> list = classroomService.getAvailableClassrooms(vo);
        return Result.success(list);
    }


}