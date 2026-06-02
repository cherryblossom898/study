package com.example.coursequerysystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.coursequerysystem.entity.Classroom;
import com.example.coursequerysystem.mapper.*;
import com.example.coursequerysystem.service.ClassroomService;
import com.example.coursequerysystem.vo.ClassroomAvailableQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class ClassroomServiceImpl extends ServiceImpl<ClassroomMapper, Classroom> implements ClassroomService {

    @Override
    public boolean save(Classroom classroom) {
        // 教室编号唯一性校验
        if (lambdaQuery().eq(Classroom::getRoomId, classroom.getRoomId()).exists()) {
            throw new RuntimeException("教室编号已存在，请重新输入");
        }
        return super.save(classroom);
    }
    @Override
    public IPage<Classroom> getClassroomPage(Integer current, Integer size, String keyword) {
        Page<Classroom> page = new Page<>(current, size);
        LambdaQueryWrapper<Classroom> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Classroom::getRoomId, keyword)
                    .or()
                    .like(Classroom::getRoomName, keyword);
        }
        return this.page(page, wrapper);
    }

    @Autowired
    private TimeScheduleMapper timeScheduleMapper;

    @Autowired
    private ClassroomMapper classroomMapper;

    @Override
    public List<Classroom> getAvailableClassrooms(ClassroomAvailableQueryVO vo) {
        // 1. 如果未传结束节次，默认与开始节次相同（单节次查询）
        String endSection = vo.getEndSection();
        if (endSection == null || endSection.isEmpty()) {
            endSection = vo.getStartSection();
        }

        // 2. 获取该时间段对应的所有 schedule_id
        List<String> scheduleIds = timeScheduleMapper.selectScheduleIdsByWeekdayAndSection(
                vo.getWeekday(), vo.getStartSection(), endSection);

        // 3. 如果没有匹配的时间安排，直接返回空列表（理论上不会，因为节次应已预定义）
        if (scheduleIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 4. 查询空闲教室
        return classroomMapper.selectAvailableClassrooms(vo.getSemester(), vo.getWeekday(), scheduleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addClassroom(Classroom classroom) {
        boolean exists = lambdaQuery().eq(Classroom::getRoomId, classroom.getRoomId()).exists();
        if (exists) {
            throw new RuntimeException("教室已存在，请重新输入");
        }
        this.save(classroom);
    }


}