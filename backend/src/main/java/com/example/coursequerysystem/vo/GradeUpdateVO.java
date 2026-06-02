package com.example.coursequerysystem.vo;

import lombok.Data;

@Data
public class GradeUpdateVO {
    private String enrollmentId;
    private Integer grade;   // 0-100
}