package com.example.coursequerysystem.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MyCourseVO {
    private String teachingId;      // 教学班ID（用于退课）
    private String courseName;      // 课程名称
    private String teacherName;     // 授课教师
    private BigDecimal credit;      // 学分
    private Integer hours;          // 学时
    private String weekday;         // 星期几（1-7）
    private String section;         // 节次（如 "1-2节"）
    private String classroom;       // 教室名称
    private String semester;        // 学期
    private Integer grade;          //课程成绩
}