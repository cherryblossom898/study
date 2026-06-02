package com.example.coursequerysystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalTime;

@Data
@TableName("time_schedule")
public class TimeSchedule {
    @TableId("schedule_id")
    private String scheduleId;

    @TableField("semester")
    private String semester;

    @TableField("week_range")
    private String weekRange;

    @TableField("week_flag")
    private String weekFlag;

    @TableField("weekday")
    private String weekday;

    @TableField("section")
    private String section;

    @TableField("start_time")
    private LocalTime startTime;

    @TableField("end_time")
    private LocalTime endTime;
}