package com.example.coursequerysystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;

@Data
@TableName("teaching")
public class Teaching {
    @TableId("teaching_id")
    private String teachingId;

    @TableField("course_id")
    private Integer courseId;      // 新关联方式

    @TableField("teacher_id")
    private String teacherId;

    @TableField("room_id")
    private String roomId;

    @TableField("schedule_id")
    private String scheduleId;

    @TableField("start_date")
    private LocalDate startDate;

    @TableField("end_date")
    private LocalDate endDate;

    @TableField("course_code")
     private String courseCode;

    @TableField("semester")
     private String semester;
}