package com.example.coursequerysystem.vo;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TeachingCreateVO {
    private Integer courseId;       // 课程ID（course表自增id）
    private String teacherId;       // 教师工号
    private String roomId;          // 教室编号
    private String scheduleId;      // 时间安排ID（time_schedule表主键）
    private String semester;        // 学期（冗余字段，便于查询）
    private LocalDate startDate;    // 授课开始日期
    private LocalDate endDate;      // 授课结束日期
}