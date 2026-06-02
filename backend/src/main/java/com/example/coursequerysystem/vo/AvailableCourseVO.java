package com.example.coursequerysystem.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AvailableCourseVO {
    private String teachingId;          // 教学班ID
    private String courseCode;          // 课程代码
    private String courseName;          // 课程名称
    private BigDecimal credit;          // 学分
    private Integer hours;              // 学时
    private String teacherName;         // 授课教师
    private String classroomName;       // 教室
    private String semester;            // 学期
    private String weekday;             // 星期几（1-7）
    private String section;             // 节次（如"1-2节"）
    private String startTime;           // 开始时间
    private String endTime;             // 结束时间
    private Integer maxStudents;        // 课容量
    private Integer currentStudents;    // 已选人数
    private Boolean isSelected;         // 当前学生是否已选（前端用来置灰按钮）
}