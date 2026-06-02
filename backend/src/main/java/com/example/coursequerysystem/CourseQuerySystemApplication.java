package com.example.coursequerysystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.coursequerysystem.mapper") // 扫描Mapper包
public class CourseQuerySystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseQuerySystemApplication.class, args);
    }
}