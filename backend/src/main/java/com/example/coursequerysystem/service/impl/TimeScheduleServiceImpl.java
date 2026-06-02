package com.example.coursequerysystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.coursequerysystem.entity.TimeSchedule;
import com.example.coursequerysystem.mapper.TimeScheduleMapper;
import com.example.coursequerysystem.service.TimeScheduleService;
import org.springframework.stereotype.Service;

@Service
public class TimeScheduleServiceImpl extends ServiceImpl<TimeScheduleMapper, TimeSchedule> implements TimeScheduleService {
}