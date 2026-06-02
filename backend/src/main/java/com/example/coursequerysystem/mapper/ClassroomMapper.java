package com.example.coursequerysystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.entity.Classroom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.coursequerysystem.vo.*;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.coursequerysystem.service.*;

import java.util.List;


public interface ClassroomMapper extends BaseMapper<Classroom> {
    /**
     * 查询指定时间段内空闲的教室
     * @param semester  学期
     * @param weekday   星期几
     * @param scheduleIds 该时间段对应的所有 time_schedule 的 ID 集合
     * @return 空闲教室列表
     */
    List<Classroom> selectAvailableClassrooms(@Param("semester") String semester,
                                              @Param("weekday") String weekday,
                                              @Param("scheduleIds") List<String> scheduleIds);



}