package com.example.coursequerysystem.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@ConfigurationProperties(prefix = "upload")
public class UploadConfig {

    private String path = "./uploads/avatars/";

    public String getPath() {
        // 如果配置的是相对路径，则基于项目根目录转换为绝对路径
        File file = new File(path);
        if (!file.isAbsolute()) {
            // 获取项目根目录（user.dir 即当前工作目录，通常为项目根目录）
            String projectRoot = System.getProperty("user.dir");
            file = new File(projectRoot, path);
        }
        // 确保目录存在
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath() + File.separator;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @PostConstruct
    public void init() {
        System.out.println("上传目录绝对路径: " + getPath());
    }


}