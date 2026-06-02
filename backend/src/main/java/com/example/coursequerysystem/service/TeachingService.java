package com.example.coursequerysystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.coursequerysystem.entity.Teaching;
import com.example.coursequerysystem.vo.*;


import java.util.List;

public interface TeachingService extends IService<Teaching> {

    IPage<TeachingVO> getTeachingPage(Integer current, Integer size, String keyword,
                                      String semester, String teacherId, String roomId);

    /**
     * 查询教师的个人授课安排（课表）
     * @param teacherId 教师工号（即 relateId）
     * @param semester  学期（可选，传 null 查询所有学期）
     * @return 教师课表数据
     */
    List<TeacherScheduleVO> getTeacherSchedule(String teacherId, String semester);

    /**
     * 新增排课（包含冲突检测）
     * @param vo 排课信息
     * @return 新生成的 teachingId
     * @throws RuntimeException 冲突或业务校验失败时抛出
     */
    String createTeaching(TeachingCreateVO vo);

    /**
     * 修改排课信息（包含冲突检测）
     * @param vo 修改参数
     * @throws RuntimeException 校验失败时抛出
     */
    void updateTeaching(TeachingUpdateVO vo);
}