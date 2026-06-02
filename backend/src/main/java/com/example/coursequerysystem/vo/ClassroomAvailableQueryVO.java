package com.example.coursequerysystem.vo;

import lombok.Data;

@Data
public class ClassroomAvailableQueryVO {
    private String semester;        // 学期（必填）
    private String weekday;         // 星期几（1-7，必填）
    private String startSection;    // 开始节次（如 "1"）
    private String endSection;      // 结束节次（可选，默认与开始相同）
}