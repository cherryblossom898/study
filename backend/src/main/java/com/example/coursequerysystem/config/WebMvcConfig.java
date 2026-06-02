package com.example.coursequerysystem.config;

import com.example.coursequerysystem.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private UploadConfig uploadConfig;   // 注入统一的上传配置

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/course/public/**",      // 课程公开查询
                        "/teacher/public/**",     // 教师公开查询
                        "/department/all",
                        "/course-type/all",
                        "/uploads/**",
                        "/upload/avatars/**",   // 新增：图片访问接口放行
                        "/error"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + uploadConfig.getPath();
        System.out.println("静态资源映射: /uploads/** -> " + location);
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
    }
}