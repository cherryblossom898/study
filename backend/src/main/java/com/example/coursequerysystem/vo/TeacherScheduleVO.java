package com.example.coursequerysystem.vo;

import lombok.Data;

@Data
public class TeacherScheduleVO {
    private String teachingId;
    private String courseName;
    private String classroomName;
    private String weekday;
    private String section;
    private String startTime;
    private String endTime;
    private String semester;
    private Integer capacity;          // 课容量
    private Integer selectedCount;     // 已选人数
    // 以下用于展示学生名单（查询详情时用）
    private String className;          // 授课班级（多个用逗号分隔）
}