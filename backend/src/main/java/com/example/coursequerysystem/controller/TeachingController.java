package com.example.coursequerysystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.coursequerysystem.common.AuthUtils;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.entity.Teaching;
import com.example.coursequerysystem.service.TeachingService;
import com.example.coursequerysystem.vo.TeachingCreateVO;
import com.example.coursequerysystem.vo.TeachingUpdateVO;
import com.example.coursequerysystem.vo.TeachingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teaching")
public class TeachingController {

    @Autowired
    private TeachingService teachingService;

    /**
     * 【管理员】分页查询排课记录（支持按学期、教师、教室筛选）
     */
    @GetMapping("/page")
    public Result<IPage<TeachingVO>> getTeachingPage(@RequestParam(defaultValue = "1") Integer current,
                                                     @RequestParam(defaultValue = "10") Integer size,
                                                     @RequestParam(required = false) String keyword,
                                                     @RequestParam(required = false) String semester,
                                                     @RequestParam(required = false) String teacherId,
                                                     @RequestParam(required = false) String roomId) {
        AuthUtils.checkAdmin();
        IPage<TeachingVO> page = teachingService.getTeachingPage(current, size, keyword, semester, teacherId, roomId);
        return Result.success(page);
    }
    /**
     * 【管理员】获取排课总数
     */
    @GetMapping("/count")
    public Result<Long> countTeachings() {
        AuthUtils.checkAdmin();
        return Result.success(teachingService.count());
    }

    /**
     * 最新的排课记录
     * @return
     */

    @GetMapping("/recent")
    public Result<List<Teaching>> getRecentTeachings() {
        // 按创建时间倒序，取前5条（Teaching 实体需有 createTime 字段，若无可按 teaching_id 倒序）
        List<Teaching> list = teachingService.lambdaQuery()
                .orderByDesc(Teaching::getTeachingId)  // 临时用 ID 倒序
                .last("LIMIT 5")
                .list();
        return Result.success(list);
    }

    /**
     * 【管理员】根据ID查询排课详情
     */
    @GetMapping("/{teachingId}")
    public Result<Teaching> getTeachingById(@PathVariable String teachingId) {
        AuthUtils.checkAdmin();
        Teaching teaching = teachingService.getById(teachingId);
        return teaching != null ? Result.success(teaching) : Result.error("排课记录不存在");
    }


    /**
     * 【管理员】新增排课（含冲突检测）
     * @param vo 排课请求参数
     * @return 新生成的 teachingId
     */
    @PostMapping
    public Result<String> createTeaching(@RequestBody TeachingCreateVO vo) {
        AuthUtils.checkAdmin();
        String teachingId = teachingService.createTeaching(vo);
        return Result.success("排课成功", teachingId);
    }

    /**
     * 【管理员】修改排课信息
     */
    @PutMapping
    public Result<String> updateTeaching(@RequestBody TeachingUpdateVO vo) {
        AuthUtils.checkAdmin();
        teachingService.updateTeaching(vo);
        return Result.success("修改成功");
    }


    /**
     * 【管理员】删除排课记录
     * @param teachingId 排课ID
     */
    @DeleteMapping("/{teachingId}")
    public Result<String> deleteTeaching(@PathVariable String teachingId) {
        AuthUtils.checkAdmin();
        boolean removed = teachingService.removeById(teachingId);
        return removed ? Result.success("删除成功") : Result.error("排课记录不存在");
    }
}