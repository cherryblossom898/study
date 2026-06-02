package com.example.coursequerysystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("enrollment")
public class Enrollment {
    @TableId("enrollment_id")
    private String enrollmentId;

    @TableField("student_id")
    private String studentId;

    @TableField("teaching_id")
    private String teachingId;

    @TableField("course_id")
    private Integer courseId;

    @TableField("enroll_time")
    private LocalDateTime enrollTime;

     @TableField("grade")
     private Integer grade;
}