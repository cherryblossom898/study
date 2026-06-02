package com.example.coursequerysystem.controller;

import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.entity.TimeSchedule;
import com.example.coursequerysystem.service.TimeScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/time-schedule")
public class TimeScheduleController {

    @Autowired
    private TimeScheduleService timeScheduleService;

    @GetMapping("/all")
    public Result<List<TimeSchedule>> getAllTimeSchedules() {
        return Result.success(timeScheduleService.list());
    }
}