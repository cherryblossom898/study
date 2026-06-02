package com.example.coursequerysystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionTest {
    public static void main(String[] args) {
        // 替换为你的数据库连接信息
        String url = "jdbc:mysql://localhost:3306/course_query_system?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("✅ 数据库连接成功！");
            // 可选：测试简单查询
            if (conn != null) {
                System.out.println("🔗 连接对象：" + conn);
            }
        } catch (SQLException e) {
            System.out.println("❌ 数据库连接失败！");
            System.out.println("错误信息：" + e.getMessage());
            e.printStackTrace();
        }
    }
}