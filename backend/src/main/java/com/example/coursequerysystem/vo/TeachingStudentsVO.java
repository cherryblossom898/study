package com.example.coursequerysystem.vo;

import lombok.Data;

@Data
public class TeachingStudentsVO {
    private String studentId;
    private String studentName;
    private String gender;
    private String major;
    private String className;    // 班级
    private String enrollTime;   // 选课时间
    private String enrollmentId;
    private Integer grade;
}