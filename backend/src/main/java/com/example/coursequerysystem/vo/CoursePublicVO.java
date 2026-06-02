package com.example.coursequerysystem.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CoursePublicVO {
    private Integer id;
    private String courseCode;
    private String courseName;
    private BigDecimal credit;
    private Integer hours;
    private String typeName;      // 课程类型名称
    private String semester;
    private Integer maxStudents;
    private Integer currentStudents;
}