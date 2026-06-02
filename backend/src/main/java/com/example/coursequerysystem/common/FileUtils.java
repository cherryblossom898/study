package com.example.coursequerysystem.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtils {

    /**
     * 保存头像文件到指定目录
     * @param file 上传的文件
     * @param uploadDir 上传根目录（从配置读取）
     * @return 返回可访问的URL相对路径（如 /uploads/avatars/xxx.jpg）
     */
    public static String saveAvatar(MultipartFile file, String uploadDir) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件为空");
        }

        // 获取原始文件名后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;

        // 创建目标目录（如果不存在）
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 保存文件
        File dest = new File(dir, fileName);
        file.transferTo(dest);

        // 返回访问URL（相对路径，配合静态资源映射）
        return "/uploads/avatars/" + fileName;
    }
}