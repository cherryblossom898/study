package com.example.coursequerysystem.common;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获运行时异常（包含业务校验抛出的异常）
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        String msg = e.getMessage();
        // 认证相关
        if ("未登录".equals(msg)) {
            return Result.error(401, "请先登录");
        }
        // 权限相关
        if ("无权限访问".equals(msg)) {
            return Result.error(403, "无权限访问");
        }
        // 业务校验异常（如学号已存在、格式错误等），统一返回 400
        if (msg != null && (msg.contains("已存在")
                || msg.contains("格式不正确")
                || msg.contains("不能为空")
                || msg.contains("不存在")
                || msg.contains("冲突"))) {
            return Result.error(400, msg);
        }
        // 其他未知运行时异常，返回 500
        return Result.error(500, "服务器内部错误：" + msg);
    }

    /**
     * 处理数据库主键或唯一键冲突异常（兜底）
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<String> handleDuplicateKeyException(DuplicateKeyException e) {
        String msg = e.getMessage();
        if (msg != null && msg.contains("student.PRIMARY")) {
            return Result.error(400, "学生已存在，请重新输入");
        }
        if (msg != null && msg.contains("teacher.PRIMARY")) {
            return Result.error(400, "教师已存在，请重新输入");
        }
        if (msg != null && msg.contains("course")) {
            return Result.error(400, "该课程在当前学期已存在，请重新输入");
        }
        if (msg != null && msg.contains("classroom.PRIMARY")) {
            return Result.error(400, "教室已存在，请重新输入");
        }
        if (msg != null && msg.contains("department.PRIMARY")) {
            return Result.error(400, "院系已存在，请重新输入");
        }
        if (msg != null && msg.contains("course_type.PRIMARY")) {
            return Result.error(400, "课程类型已存在，请重新输入");
        }
        return Result.error(400, "数据重复，操作失败");
    }
}