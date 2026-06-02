package com.example.coursequerysystem.vo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TeachingVO {
    private String teachingId;
    private Integer courseId;
    private String courseName;
    private String teacherId;
    private String teacherName;
    private String roomId;
    private String roomName;
    private String scheduleId;
    private String weekday;
    private String section;
    private LocalTime startTime;
    private LocalTime endTime;
    private String semester;
    private LocalDate startDate;
    private LocalDate endDate;
}