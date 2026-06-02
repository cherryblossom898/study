package com.example.coursequerysystem.vo;

import lombok.Data;

@Data
public class TeacherPublicVO {
    private String teacherId;
    private String teacherName;
    private String gender;
    private String title;
    private String departmentName;   // 院系名称
}