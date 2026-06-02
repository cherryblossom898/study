package com.example.coursequerysystem.vo;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TeachingUpdateVO {
    private String teachingId;      // 必传：要修改的排课ID
    private Integer courseId;       // 可选
    private String teacherId;       // 可选
    private String roomId;          // 可选
    private String scheduleId;      // 可选
    private String semester;        // 可选
    private LocalDate startDate;    // 可选
    private LocalDate endDate;      // 可选
}