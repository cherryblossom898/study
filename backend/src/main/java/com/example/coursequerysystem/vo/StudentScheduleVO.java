package com.example.coursequerysystem.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StudentScheduleVO {
    private String teachingId;          // 授课安排ID
    private Integer courseId;           // 课程ID
    private String courseName;          // 课程名称
    private String teacherName;         // 授课教师
    private String classroomName;       // 教室名称
    private String weekday;             // 星期几（1-7）
    private String section;             // 节次（如"1-2节"）
    private String startTime;           // 开始时间（格式化）
    private String endTime;             // 结束时间（格式化）
    private BigDecimal credit;          // 学分
    private Integer hours;              // 学时
    private String semester;            // 学期
}