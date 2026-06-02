# 高校排课管理系统

> 数据库课程设计项目 | Spring Boot + Vue 3 前后端分离

## 项目简介

本项目是一个面向高校的排课与选课管理系统，支持学生、教师、管理员三类角色的协同工作。系统实现了课程管理、学生选课与退课、教师授课安排、管理员排课调度以及全局数据统计等核心功能，旨在提升教务管理效率，优化教学资源配置。

## 技术栈

|层级|技术|版本/说明|
|---|---|---|
|**前端**|Vue 3|组合式 API|
||Vite|构建工具|
||Element Plus|UI 组件库|
||Axios|HTTP 请求库|
||Pinia|状态管理|
|**后端**|Spring Boot|3.x|
||MyBatis-Plus|数据持久层|
||MySQL|8.0+|
||JWT|身份认证|
|**其他**|Maven|项目构建|
||Redis|缓存（可选）|

## 功能模块

### 游客端（公共查询）

- 浏览全部课程信息
    
- 浏览全部教师信息
    
- 查询空闲教室
    

### 学生端

- 登录 / 修改密码
    
- 查看 / 修改个人信息，上传头像
    
- 按学期查看个人课表
    
- 浏览可选课程并选课（自动校验时间冲突、容量上限）
    
- 查看已选课程及成绩（有成绩则不可退课）
    
- 退选课程
    

### 教师端

- 登录 / 修改密码
    
- 查看 / 修改个人信息，上传头像
    
- 按学期查看个人授课课表
    
- 查看所授课程的学生名单
    
- 录入 / 修改学生成绩（0-100 分）
    

### 管理员端

- 仪表盘：学生/教师/课程/教室总数、热门课程、最新排课
    
- 学生管理：增删改查，学号格式校验（9位数字）
    
- 教师管理：增删改查
    
- 课程管理：增删改查，课程代码格式校验（如 C001）
    
- 教室管理：增删改查
    
- 院系 / 课程类型管理
    
- 排课管理：新增 / 修改 / 删除排课，自动检测教师、教室时间冲突
    

## 项目结构

```
CourseQuerySystem
├── course-query-frontend          # 前端项目
│   ├── public
│   ├── src
│   │   ├── api                    # 接口封装
│   │   ├── assets                 # 静态资源
│   │   ├── components             # 公共组件
│   │   ├── layout                 # 布局组件
│   │   ├── router                 # 路由配置
│   │   ├── stores                 # Pinia 状态管理
│   │   ├── utils                  # 工具函数（Axios 封装等）
│   │   ├── views                  # 页面组件
│   │   │   ├── login              # 登录页
│   │   │   ├── student            # 学生端
│   │   │   ├── teacher            # 教师端
│   │   │   ├── admin              # 管理员端
│   │   │   ├── public             # 公共查询
│   │   │   └── error              # 错误页
│   │   ├── App.vue
│   │   └── main.js
│   ├── package.json
│   └── vite.config.js
│
└── course-query-system            # 后端项目（Spring Boot）
    ├── src/main/java/com/example/coursequerysystem
    │   ├── common                  # 通用工具（Result, AuthUtils, UserContext 等）
    │   ├── config                  # 配置类（CORS, MyBatis-Plus, 上传配置）
    │   ├── controller              # 控制器
    │   ├── entity                  # 实体类
    │   ├── interceptor             # 拦截器（Token 校验）
    │   ├── mapper                  # Mapper 接口
    │   ├── service                 # 业务接口
    │   │   └── impl                # 业务实现
    │   ├── utils                   # 工具类（JWT, FileUtils）
    │   └── vo                      # 视图对象
    └── src/main/resources
        ├── application.yml         # 主配置文件
        └── mapper                  # Mapper XML 映射文件
```

## 快速开始

### 环境要求

- JDK 17+   - JDK 17
    
- Node.js 18+
    
- MySQL 8.0+   - MySQL 8.0
    
- Redis 6.0+（可选，仅需缓存时配置）
    

### 数据库配置

1. 创建数据库：
    
    sql
    
    CREATE DATABASE course_query_system CHARACTER SET utf8mb4;创建数据库course_query_system字符集utf8mb4
    
2. 执行项目提供的 SQL 脚本 `init.sql`（建表 + 测试数据）。
    
3. 修改后端 `application.yml` 中的数据库连接信息（用户名、密码等）。
    

### 后端运行

bash

cd course-query-system
mvn clean install
mvn spring-boot:run

默认运行于 `http://localhost:8080`，API 前缀为 `/api`。

### 前端运行

bash

cd course-query-frontend
npm install
npm run dev

默认运行于 `http://localhost:5173`，代理配置已在 `vite.config.js` 中完成，开发环境下自动转发 `/api` 请求至后端。

### Redis 缓存配置（可选）

如需启用 Redis 缓存，确保本地 Redis 服务已启动，并在 `application.yml` 中添加连接信息。代码中已包含 Spring Cache 注解，可直接使用。

## 测试账号

|角色|用户名|密码|
|---|---|---|
|管理员|admin|123456|
|学生|202400101|123456|
|教师|T001|123456|

> 更多测试数据可查阅 `init.sql` 中的 `user` 表。

## 界面预览

> 可根据需要在此处插入系统主要界面截图（登录页、学生课表、管理员排课管理等）。

## 主要特性

- **前后端分离**：RESTful API，清晰的职责划分。
    
- **JWT 认证**：无状态用户认证，支持角色鉴权。
    
- **冲突检测**：选课容量与时间冲突、排课教师与教室冲突自动校验。
    
- **文件上传**：用户头像上传，支持本地存储与访问。
    
- **数据校验**：前后端双重校验，友好的错误提示。
    
- **响应式布局**：Element Plus 组件提供一致的 UI 体验。
    

## 注意事项

- 项目为课程设计作业，部分逻辑（如 Token 存储、冲突检测精度）已做简化处理，实际生产环境建议升级为分布式缓存（Redis）和更精细的冲突判断算法。
    
- 数据库密码等敏感信息已从公共仓库移除，请自行配置。
    
- 首次启动前端若报跨域错误，请确认后端已正确配置 CORS。
    

## License

MIT License

---

欢迎 Star ⭐ 和提出 Issue！
