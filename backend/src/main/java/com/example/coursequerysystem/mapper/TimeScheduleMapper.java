package com.example.coursequerysystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.coursequerysystem.entity.TimeSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface TimeScheduleMapper extends BaseMapper<TimeSchedule> {
    /**
     * 根据星期和节次范围查询时间安排ID列表
     * @param weekday      星期几
     * @param startSection 开始节次（如 "1"）
     * @param endSection   结束节次（如 "2"）
     * @return schedule_id 列表
     */
    List<String> selectScheduleIdsByWeekdayAndSection(@Param("weekday") String weekday,
                                                      @Param("startSection") String startSection,
                                                      @Param("endSection") String endSection);
}