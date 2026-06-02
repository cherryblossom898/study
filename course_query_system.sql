/*
 Navicat Premium Dump SQL

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 90500 (9.5.0)
 Source Host           : localhost:3306
 Source Schema         : course_query_system

 Target Server Type    : MySQL
 Target Server Version : 90500 (9.5.0)
 File Encoding         : 65001

 Date: 02/06/2026 22:17:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for classroom
-- ----------------------------
DROP TABLE IF EXISTS `classroom`;
CREATE TABLE `classroom`  (
  `room_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教室编号（主键）',
  `room_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教室名称（如1号教学楼101）',
  `capacity` int NOT NULL COMMENT '教室容量',
  PRIMARY KEY (`room_id`) USING BTREE,
  CONSTRAINT `classroom_chk_1` CHECK (`capacity` > 0)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教室信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of classroom
-- ----------------------------
INSERT INTO `classroom` VALUES ('R001', '1号教学楼101', 50);
INSERT INTO `classroom` VALUES ('R002', '1号教学楼102', 60);
INSERT INTO `classroom` VALUES ('R003', '1号教学楼201', 45);
INSERT INTO `classroom` VALUES ('R004', '2号教学楼301', 80);
INSERT INTO `classroom` VALUES ('R005', '2号教学楼302', 50);
INSERT INTO `classroom` VALUES ('R006', '实验楼101', 30);
INSERT INTO `classroom` VALUES ('R007', '实验楼202', 25);
INSERT INTO `classroom` VALUES ('R008', '综合楼401', 100);
INSERT INTO `classroom` VALUES ('R009', '多媒体教室1', 40);
INSERT INTO `classroom` VALUES ('R010', '阶梯教室1', 150);

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_code` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程代码（主键）',
  `course_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
  `credit` decimal(2, 1) NOT NULL COMMENT '课程学分（如2.0、3.5）',
  `hours` int NOT NULL COMMENT '课程学时',
  `type_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程类型编号（外键）',
  `department_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '开课院系编号（外键）',
  `semester` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '开课学期（如2025-2026学年第一学期）',
  `max_students` int NOT NULL COMMENT '最大选课人数',
  `current_students` int NOT NULL DEFAULT 0 COMMENT '当前选课人数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_course_code_semester`(`course_code` ASC, `semester` ASC) USING BTREE,
  INDEX `type_id`(`type_id` ASC) USING BTREE,
  INDEX `idx_course_department`(`department_id` ASC) USING BTREE,
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `course_type` (`type_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `course_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `course_chk_1` CHECK (`credit` > 0),
  CONSTRAINT `course_chk_2` CHECK (`hours` > 0),
  CONSTRAINT `course_chk_3` CHECK (`max_students` > 0),
  CONSTRAINT `course_chk_4` CHECK ((`current_students` >= 0) and (`current_students` <= `max_students`))
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, 'C001', 'Python程序设计', 3.0, 48, 'T003', 'D001', '2024-2025学年第一学期', 60, 34);
INSERT INTO `course` VALUES (2, 'C002', '电路原理', 4.0, 64, 'T001', 'D002', '2024-2025学年第一学期', 50, 34);
INSERT INTO `course` VALUES (3, 'C003', '市场营销学', 2.5, 40, 'T003', 'D003', '2024-2025学年第一学期', 45, 22);
INSERT INTO `course` VALUES (4, 'C004', '大学英语（四）', 3.0, 48, 'T001', 'D004', '2024-2025学年第一学期', 80, 56);
INSERT INTO `course` VALUES (5, 'C005', '中国古代文学', 2.0, 32, 'T004', 'D005', '2024-2025学年第一学期', 40, 21);
INSERT INTO `course` VALUES (6, 'C006', '高等数学（下）', 4.5, 72, 'T001', 'D006', '2024-2025学年第一学期', 100, 64);
INSERT INTO `course` VALUES (7, 'C007', '解剖学基础', 5.0, 80, 'T003', 'D007', '2024-2025学年第一学期', 30, 24);
INSERT INTO `course` VALUES (8, 'C008', '素描基础', 2.0, 32, 'T002', 'D008', '2024-2025学年第一学期', 25, 12);
INSERT INTO `course` VALUES (9, 'C009', '民法总论', 3.5, 56, 'T003', 'D009', '2024-2025学年第一学期', 50, 30);
INSERT INTO `course` VALUES (10, 'C010', '体育理论', 1.0, 16, 'T006', 'D010', '2024-2025学年第一学期', 150, 83);

-- ----------------------------
-- Table structure for course_type
-- ----------------------------
DROP TABLE IF EXISTS `course_type`;
CREATE TABLE `course_type`  (
  `type_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程类型编号（主键）',
  `type_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程类型名称（如必修、选修、专业核心）',
  PRIMARY KEY (`type_id`) USING BTREE,
  UNIQUE INDEX `uk_type_name`(`type_name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_type
-- ----------------------------
INSERT INTO `course_type` VALUES ('T003', '专业核心课');
INSERT INTO `course_type` VALUES ('T004', '公共基础课');
INSERT INTO `course_type` VALUES ('T009', '实习课');
INSERT INTO `course_type` VALUES ('T005', '实践课');
INSERT INTO `course_type` VALUES ('T007', '实验课');
INSERT INTO `course_type` VALUES ('T001', '必修课');
INSERT INTO `course_type` VALUES ('T010', '毕业设计');
INSERT INTO `course_type` VALUES ('T008', '研讨课');
INSERT INTO `course_type` VALUES ('T002', '选修课');
INSERT INTO `course_type` VALUES ('T006', '通识教育课');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `department_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '院系编号（主键）',
  `department_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '院系名称',
  `department_dean` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '院系负责人',
  `department_phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `department_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '办公地址',
  PRIMARY KEY (`department_id`) USING BTREE,
  UNIQUE INDEX `uk_department_name`(`department_name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '院系信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('D001', '计算机学院', '张教授', '010-12345601', '1号办公楼301室');
INSERT INTO `department` VALUES ('D002', '电子工程学院', '李教授', '010-12345602', '1号办公楼302室');
INSERT INTO `department` VALUES ('D003', '工商管理学院', '王副教授', '010-12345603', '2号办公楼201室');
INSERT INTO `department` VALUES ('D004', '外国语学院', '赵讲师', '010-12345604', '2号办公楼202室');
INSERT INTO `department` VALUES ('D005', '文学院', '孙教授', '010-12345605', '3号办公楼101室');
INSERT INTO `department` VALUES ('D006', '理学院', '周副教授', '010-12345606', '3号办公楼102室');
INSERT INTO `department` VALUES ('D007', '医学院', '吴教授', '010-12345607', '4号办公楼401室');
INSERT INTO `department` VALUES ('D008', '艺术学院', '郑讲师', '010-12345608', '4号办公楼402室');
INSERT INTO `department` VALUES ('D009', '法学院', '马副教授', '010-12345609', '5号办公楼301室');
INSERT INTO `department` VALUES ('D010', '体育学院', '冯教授', '010-12345610', '体育馆201室');

-- ----------------------------
-- Table structure for enrollment
-- ----------------------------
DROP TABLE IF EXISTS `enrollment`;
CREATE TABLE `enrollment`  (
  `enrollment_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '选课记录ID（主键）',
  `student_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号（外键）',
  `teaching_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '授课安排ID',
  `enroll_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
  `grade` int NULL DEFAULT NULL COMMENT '成绩（0-100分，可选）',
  `course_id` int NOT NULL,
  PRIMARY KEY (`enrollment_id`) USING BTREE,
  UNIQUE INDEX `uk_student_teaching`(`student_id` ASC, `teaching_id` ASC) USING BTREE,
  INDEX `idx_enrollment_student`(`student_id` ASC) USING BTREE,
  INDEX `fk_enrollment_teaching`(`teaching_id` ASC) USING BTREE,
  INDEX `fk_enrollment_course`(`course_id` ASC) USING BTREE,
  CONSTRAINT `enrollment_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_enrollment_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_enrollment_teaching` FOREIGN KEY (`teaching_id`) REFERENCES `teaching` (`teaching_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `enrollment_chk_1` CHECK ((`grade` is null) or (`grade` between 0 and 100))
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '选课记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enrollment
-- ----------------------------
INSERT INTO `enrollment` VALUES ('E1776252349874', '202400301', 'TE001', '2026-04-15 19:25:50', NULL, 1);
INSERT INTO `enrollment` VALUES ('E1776252350696', '202400301', 'TE003', '2026-04-15 19:25:51', NULL, 3);
INSERT INTO `enrollment` VALUES ('E1776252351576', '202400301', 'TE004', '2026-04-15 19:25:52', NULL, 4);
INSERT INTO `enrollment` VALUES ('E1776252352099', '202400301', 'TE002', '2026-04-15 19:25:52', NULL, 2);
INSERT INTO `enrollment` VALUES ('E1776252352809', '202400301', 'TE005', '2026-04-15 19:25:53', NULL, 5);
INSERT INTO `enrollment` VALUES ('E1776252354199', '202400301', 'TE006', '2026-04-15 19:25:54', NULL, 6);
INSERT INTO `enrollment` VALUES ('E1776252373461', '202400401', 'TE001', '2026-04-15 19:26:13', NULL, 1);
INSERT INTO `enrollment` VALUES ('E1776252373946', '202400401', 'TE002', '2026-04-15 19:26:14', NULL, 2);
INSERT INTO `enrollment` VALUES ('E1776252375701', '202400401', 'TE1766674748728', '2026-04-15 19:26:16', NULL, 1);
INSERT INTO `enrollment` VALUES ('E1776252376304', '202400401', 'TE004', '2026-04-15 19:26:16', NULL, 4);
INSERT INTO `enrollment` VALUES ('E1776252377974', '202400401', 'TE005', '2026-04-15 19:26:18', NULL, 5);
INSERT INTO `enrollment` VALUES ('E1776252379035', '202400401', 'TE007', '2026-04-15 19:26:19', NULL, 7);
INSERT INTO `enrollment` VALUES ('E1776772726963', '202400101', 'TE006', '2026-04-21 19:58:47', NULL, 6);
INSERT INTO `enrollment` VALUES ('E1776772727336', '202400101', 'TE002', '2026-04-21 19:58:47', NULL, 2);
INSERT INTO `enrollment` VALUES ('E1776772737755', '202400101', 'TE010', '2026-04-21 19:58:58', NULL, 10);
INSERT INTO `enrollment` VALUES ('E1776772738593', '202400101', 'TE1766674748728', '2026-04-21 19:58:59', NULL, 1);
INSERT INTO `enrollment` VALUES ('E1776772758304', '202400101', 'TE004', '2026-04-21 19:59:18', NULL, 4);
INSERT INTO `enrollment` VALUES ('E1776772759008', '202400101', 'TE009', '2026-04-21 19:59:19', NULL, 9);
INSERT INTO `enrollment` VALUES ('E1776772759376', '202400101', 'TE005', '2026-04-21 19:59:19', NULL, 5);
INSERT INTO `enrollment` VALUES ('E1776772760583', '202400101', 'TE008', '2026-04-21 19:59:21', NULL, 8);
INSERT INTO `enrollment` VALUES ('E1776842655476', '202400101', 'TE001', '2026-04-22 15:24:15', NULL, 1);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `student_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号（主键）',
  `student_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `gender` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '性别（男/女）',
  `birth_date` datetime NULL DEFAULT NULL COMMENT '出生日期',
  `enrollment_year` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '入学年份（如2024）',
  `grade` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '年级（如2024级）',
  `major` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专业',
  `class` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级（如区块链1班）',
  `student_phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `department_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属院系编号（外键）',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像图片URL',
  PRIMARY KEY (`student_id`) USING BTREE,
  INDEX `idx_student_department`(`department_id` ASC) USING BTREE,
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `student_chk_1` CHECK (`gender` in (_utf8mb4'男',_utf8mb4'女'))
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('202400101', '张三', '男', '2005-03-15 00:00:00', '2024', '2024级', '计算机科学与技术', '计科1班', '13800138001', 'D001', '/uploads/avatars/79129309eb7444aab45601a37e269238.jpg');
INSERT INTO `student` VALUES ('202400301', '赵六', '女', '2005-09-10 00:00:00', '2024', '2024级', '工商管理', '工商1班', '13800138004', 'D003', NULL);
INSERT INTO `student` VALUES ('202400401', '孙七', '男', '2005-11-25 00:00:00', '2024', '2024级', '英语', '英语1班', '13800138005', 'D004', NULL);
INSERT INTO `student` VALUES ('202400501', '周八', '女', '2005-07-30 00:00:00', '2024', '2024级', '汉语言文学', '文产1班', '13800138006', 'D005', NULL);
INSERT INTO `student` VALUES ('202400601', '吴九', '男', '2005-04-12 00:00:00', '2024', '2024级', '数学与应用数学', '数科1班', '13800138007', 'D006', NULL);
INSERT INTO `student` VALUES ('202400701', '郑十', '女', '2005-08-18 00:00:00', '2024', '2024级', '临床医学', '临床1班', '13800138008', 'D007', NULL);
INSERT INTO `student` VALUES ('202400801', '钱十一', '男', '2005-02-22 00:00:00', '2024', '2024级', '视觉传达设计', '视传1班', '13800138009', 'D008', NULL);
INSERT INTO `student` VALUES ('202400901', '孙十二', '女', '2005-10-08 00:00:00', '2024', '2024级', '法学', '法学1班', '13800138010', 'D009', NULL);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `teacher_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教师工号（主键）',
  `teacher_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `gender` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '性别（男/女）',
  `title` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职称（如讲师、教授）',
  `department_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属院系编号（外键）',
  `teacher_phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `hire_date` datetime NULL DEFAULT NULL COMMENT '入职日期',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像图片URL',
  PRIMARY KEY (`teacher_id`) USING BTREE,
  INDEX `idx_teacher_department`(`department_id` ASC) USING BTREE,
  CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `teacher_chk_1` CHECK (`gender` in (_utf8mb4'男',_utf8mb4'女'))
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教师信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('T001', '张伟', '男', '教授', 'D001', '18919291279', '2000-09-01 00:00:00', '/uploads/avatars/1c52c4ffcc04494a89df3d5078dd12ba.png');
INSERT INTO `teacher` VALUES ('T002', '李娜', '女', '副教授', 'D001', '13900139002', '2005-09-01 00:00:00', NULL);
INSERT INTO `teacher` VALUES ('T003', '王强', '男', '讲师', 'D002', '13900139003', '2010-09-01 00:00:00', NULL);
INSERT INTO `teacher` VALUES ('T004', '赵芳', '女', '教授', 'D003', '13900139004', '1998-09-01 00:00:00', NULL);
INSERT INTO `teacher` VALUES ('T005', '孙浩', '男', '副教授', 'D004', '13900139005', '2006-09-01 00:00:00', NULL);
INSERT INTO `teacher` VALUES ('T006', '周丽', '女', '讲师', 'D005', '13900139006', '2012-09-01 00:00:00', NULL);
INSERT INTO `teacher` VALUES ('T007', '吴刚', '男', '教授', 'D006', '13900139007', '1995-09-01 00:00:00', NULL);
INSERT INTO `teacher` VALUES ('T008', '郑艳', '女', '副教授', 'D007', '13900139008', '2008-09-01 00:00:00', NULL);
INSERT INTO `teacher` VALUES ('T009', '马明', '男', '讲师', 'D008', '13900139009', '2015-09-01 00:00:00', NULL);
INSERT INTO `teacher` VALUES ('T010', '冯杰', '男', '教授', 'D009', '13900139010', '2002-09-01 00:00:00', NULL);

-- ----------------------------
-- Table structure for teaching
-- ----------------------------
DROP TABLE IF EXISTS `teaching`;
CREATE TABLE `teaching`  (
  `teaching_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '授课安排ID（主键）',
  `course_code` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '授课课程代码（外键）',
  `semester` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '开课学期（关联课程表联合主键）',
  `teacher_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '授课教师工号（外键）',
  `room_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '授课教室编号（外键）',
  `schedule_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联时间安排ID（外键）',
  `start_date` date NOT NULL COMMENT '授课开始日期',
  `end_date` date NOT NULL COMMENT '授课结束日期',
  `course_id` int NOT NULL,
  PRIMARY KEY (`teaching_id`) USING BTREE,
  UNIQUE INDEX `uk_teacher_time`(`teacher_id` ASC, `schedule_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_classroom_time`(`room_id` ASC, `schedule_id` ASC) USING BTREE,
  INDEX `schedule_id`(`schedule_id` ASC) USING BTREE,
  INDEX `idx_teaching_teacher`(`teacher_id` ASC) USING BTREE,
  INDEX `idx_teaching_course`(`course_code` ASC, `semester` ASC) USING BTREE,
  INDEX `fk_teaching_course`(`course_id` ASC) USING BTREE,
  CONSTRAINT `fk_teaching_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `teaching_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`teacher_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `teaching_ibfk_3` FOREIGN KEY (`room_id`) REFERENCES `classroom` (`room_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `teaching_ibfk_4` FOREIGN KEY (`schedule_id`) REFERENCES `time_schedule` (`schedule_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `teaching_chk_1` CHECK (`start_date` < `end_date`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '授课安排表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teaching
-- ----------------------------
INSERT INTO `teaching` VALUES ('TE001', 'C001', '2024-2025学年第一学期', 'T001', 'R001', 'S001', '2024-09-02', '2024-12-20', 1);
INSERT INTO `teaching` VALUES ('TE002', 'C002', '2024-2025学年第一学期', 'T003', 'R002', 'S002', '2024-09-03', '2024-12-21', 2);
INSERT INTO `teaching` VALUES ('TE003', 'C003', '2024-2025学年第一学期', 'T004', 'R003', 'S003', '2024-09-04', '2024-12-22', 3);
INSERT INTO `teaching` VALUES ('TE004', 'C004', '2024-2025学年第一学期', 'T005', 'R004', 'S004', '2024-09-05', '2024-12-23', 4);
INSERT INTO `teaching` VALUES ('TE005', 'C005', '2024-2025学年第一学期', 'T006', 'R005', 'S005', '2024-09-06', '2024-12-24', 5);
INSERT INTO `teaching` VALUES ('TE006', 'C006', '2024-2025学年第一学期', 'T007', 'R008', 'S006', '2024-09-02', '2024-11-10', 6);
INSERT INTO `teaching` VALUES ('TE007', 'C007', '2024-2025学年第一学期', 'T008', 'R006', 'S007', '2024-11-11', '2024-12-20', 7);
INSERT INTO `teaching` VALUES ('TE008', 'C008', '2024-2025学年第一学期', 'T009', 'R007', 'S008', '2024-09-04', '2024-12-21', 8);
INSERT INTO `teaching` VALUES ('TE009', 'C009', '2024-2025学年第一学期', 'T010', 'R009', 'S009', '2024-09-05', '2024-12-22', 9);
INSERT INTO `teaching` VALUES ('TE010', 'C010', '2024-2025学年第一学期', 'T002', 'R010', 'S010', '2024-09-03', '2024-12-23', 10);
INSERT INTO `teaching` VALUES ('TE1766674748728', 'C001', '2024-2025学年第一学期', 'T001', 'R001', 'S003', '2024-09-03', '2024-12-19', 1);

-- ----------------------------
-- Table structure for time_schedule
-- ----------------------------
DROP TABLE IF EXISTS `time_schedule`;
CREATE TABLE `time_schedule`  (
  `schedule_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '时间安排ID（主键）',
  `semester` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '开课学期',
  `week_range` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '周次范围（如1-18周）',
  `week_flag` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '全周' COMMENT '单周标识（全周/单周/双周）',
  `weekday` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '星期几（1-7对应周一至周日）',
  `section` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节次（如1-2节、3-4节）',
  `start_time` time NOT NULL COMMENT '开始时间（如08:00:00）',
  `end_time` time NOT NULL COMMENT '结束时间（如09:40:00）',
  PRIMARY KEY (`schedule_id`) USING BTREE,
  CONSTRAINT `time_schedule_chk_1` CHECK (`weekday` between _utf8mb4'1' and _utf8mb4'7'),
  CONSTRAINT `time_schedule_chk_2` CHECK (`start_time` < `end_time`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '时间安排表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of time_schedule
-- ----------------------------
INSERT INTO `time_schedule` VALUES ('S001', '2024-2025学年第一学期', '1-18周', '全周', '1', '1-2节', '08:00:00', '09:40:00');
INSERT INTO `time_schedule` VALUES ('S002', '2024-2025学年第一学期', '1-18周', '全周', '2', '3-4节', '10:00:00', '11:40:00');
INSERT INTO `time_schedule` VALUES ('S003', '2024-2025学年第一学期', '1-18周', '单周', '3', '5-6节', '14:00:00', '15:40:00');
INSERT INTO `time_schedule` VALUES ('S004', '2024-2025学年第一学期', '1-18周', '双周', '4', '7-8节', '16:00:00', '17:40:00');
INSERT INTO `time_schedule` VALUES ('S005', '2024-2025学年第一学期', '1-18周', '全周', '5', '9-10节', '19:00:00', '20:40:00');
INSERT INTO `time_schedule` VALUES ('S006', '2024-2025学年第一学期', '1-9周', '全周', '1', '3-4节', '10:00:00', '11:40:00');
INSERT INTO `time_schedule` VALUES ('S007', '2024-2025学年第一学期', '10-18周', '全周', '2', '5-6节', '14:00:00', '15:40:00');
INSERT INTO `time_schedule` VALUES ('S008', '2024-2025学年第一学期', '1-18周', '单周', '4', '1-2节', '08:00:00', '09:40:00');
INSERT INTO `time_schedule` VALUES ('S009', '2024-2025学年第一学期', '1-18周', '双周', '5', '7-8节', '16:00:00', '17:40:00');
INSERT INTO `time_schedule` VALUES ('S010', '2024-2025学年第一学期', '1-18周', '全周', '3', '9-10节', '19:00:00', '20:40:00');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID（主键）',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录账号（唯一）',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '123456' COMMENT '登录密码',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色：admin/student/teacher',
  `relate_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联ID（学生学号/教师工号）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `idx_relate_id`(`relate_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('U000000001', 'admin', '123456', 'admin', NULL, '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000002', '202400101', '123456', 'student', '202400101', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000004', '202400201', '123456', 'student', '202400201', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000005', '202400301', '123456', 'student', '202400301', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000006', '202400401', '123456', 'student', '202400401', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000007', '202400501', '123456', 'student', '202400501', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000008', '202400601', '123456', 'student', '202400601', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000009', '202400701', '123456', 'student', '202400701', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000010', '202400801', '123456', 'student', '202400801', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000011', '202400901', '123456', 'student', '202400901', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000012', 'T001', '123456', 'teacher', 'T001', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000013', 'T002', '123456', 'teacher', 'T002', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000014', 'T003', '123456', 'teacher', 'T003', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000015', 'T004', '123456', 'teacher', 'T004', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000016', 'T005', '123456', 'teacher', 'T005', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000017', 'T006', '123456', 'teacher', 'T006', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000018', 'T007', '123456', 'teacher', 'T007', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000019', 'T008', '123456', 'teacher', 'T008', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000020', 'T009', '123456', 'teacher', 'T009', '2026-04-13 23:26:04', 1);
INSERT INTO `user` VALUES ('U000000021', 'T010', '123456', 'teacher', 'T010', '2026-04-13 23:26:04', 1);

-- ----------------------------
-- View structure for student_schedule_view
-- ----------------------------
DROP VIEW IF EXISTS `student_schedule_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `student_schedule_view` AS select `s`.`student_id` AS `学号`,`s`.`student_name` AS `姓名`,`c`.`course_name` AS `课程名称`,`t`.`teacher_name` AS `授课教师`,`cr`.`room_name` AS `上课地点`,`ts`.`weekday` AS `星期`,`ts`.`section` AS `节次`,`ts`.`start_time` AS `开始时间`,`ts`.`end_time` AS `结束时间`,`ts`.`week_range` AS `周次范围`,`te`.`start_date` AS `开始日期`,`te`.`end_date` AS `结束日期` from ((((((`student` `s` join `enrollment` `e` on((`s`.`student_id` = `e`.`student_id`))) join `teaching` `te` on(((`e`.`course_code` = `te`.`course_code`) and (`e`.`semester` = `te`.`semester`)))) join `course` `c` on(((`te`.`course_code` = `c`.`course_code`) and (`te`.`semester` = `c`.`semester`)))) join `teacher` `t` on((`te`.`teacher_id` = `t`.`teacher_id`))) join `classroom` `cr` on((`te`.`room_id` = `cr`.`room_id`))) join `time_schedule` `ts` on((`te`.`schedule_id` = `ts`.`schedule_id`))) where (`c`.`semester` = '2024-2025学年第一学期');

-- ----------------------------
-- View structure for teacher_course_view
-- ----------------------------
DROP VIEW IF EXISTS `teacher_course_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `teacher_course_view` AS select `t`.`teacher_id` AS `教师工号`,`t`.`teacher_name` AS `教师姓名`,`d`.`department_name` AS `所属院系`,count(distinct `te`.`course_code`) AS `授课课程数`,group_concat(distinct `c`.`course_name` separator '、') AS `授课课程`,sum(`c`.`hours`) AS `总学时`,sum(`c`.`credit`) AS `总学分` from (((`teacher` `t` join `department` `d` on((`t`.`department_id` = `d`.`department_id`))) left join `teaching` `te` on((`t`.`teacher_id` = `te`.`teacher_id`))) left join `course` `c` on(((`te`.`course_code` = `c`.`course_code`) and (`te`.`semester` = `c`.`semester`)))) where (`te`.`semester` = '2024-2025学年第一学期') group by `t`.`teacher_id`,`t`.`teacher_name`,`d`.`department_name` order by `t`.`teacher_id`;

-- ----------------------------
-- View structure for transcript_view
-- ----------------------------
DROP VIEW IF EXISTS `transcript_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `transcript_view` AS select `s`.`student_id` AS `学号`,`s`.`student_name` AS `姓名`,`d`.`department_name` AS `所属院系`,`s`.`major` AS `专业`,`c`.`course_code` AS `课程代码`,`c`.`course_name` AS `课程名称`,`ct`.`type_name` AS `课程类型`,`c`.`credit` AS `学分`,`c`.`hours` AS `学时`,`e`.`grade` AS `成绩`,(case when (`e`.`grade` >= 90) then 4.0 when (`e`.`grade` >= 80) then 3.0 when (`e`.`grade` >= 70) then 2.0 when (`e`.`grade` >= 60) then 1.0 else 0 end) AS `绩点`,`e`.`semester` AS `学期` from ((((`student` `s` join `department` `d` on((`s`.`department_id` = `d`.`department_id`))) join `enrollment` `e` on((`s`.`student_id` = `e`.`student_id`))) join `course` `c` on(((`e`.`course_code` = `c`.`course_code`) and (`e`.`semester` = `c`.`semester`)))) join `course_type` `ct` on((`c`.`type_id` = `ct`.`type_id`))) where (`e`.`grade` is not null) order by `s`.`student_id`,`e`.`semester`,`c`.`course_code`;

-- ----------------------------
-- Procedure structure for find_available_classroom
-- ----------------------------
DROP PROCEDURE IF EXISTS `find_available_classroom`;
delimiter ;;
CREATE PROCEDURE `find_available_classroom`(IN p_weekday CHAR(2),
    IN p_section VARCHAR(10),
    IN p_week_range VARCHAR(10),
    IN p_semester VARCHAR(20))
BEGIN
    SELECT 
        cr.room_id AS 教室编号,
        cr.room_name AS 教室名称,
        cr.capacity AS 容量,
        d.department_name AS 所属院系
    FROM classroom cr
    LEFT JOIN teaching te ON cr.room_id = te.room_id
    LEFT JOIN time_schedule ts ON te.schedule_id = ts.schedule_id
    LEFT JOIN course c ON te.course_code = c.course_code AND te.semester = c.semester
    LEFT JOIN department d ON c.department_id = d.department_id
    WHERE (te.room_id IS NULL 
           OR ts.schedule_id NOT IN (
               SELECT schedule_id 
               FROM time_schedule 
               WHERE weekday = p_weekday 
                 AND section = p_section 
                 AND week_range = p_week_range
                 AND semester = p_semester
           ))
      AND cr.room_id IS NOT NULL
    GROUP BY cr.room_id, cr.room_name, cr.capacity, d.department_name
    ORDER BY cr.capacity;
END
;;
delimiter ;

-- ----------------------------
-- Event structure for clean_old_courses
-- ----------------------------
DROP EVENT IF EXISTS `clean_old_courses`;
delimiter ;;
CREATE EVENT `clean_old_courses`
ON SCHEDULE
EVERY '1' MONTH STARTS '2024-12-01 02:00:00'
DO BEGIN
    DECLARE old_semester VARCHAR(20);
    
    -- 获取上一学期的学期标识
    SET old_semester = CONCAT(YEAR(DATE_SUB(NOW(), INTERVAL 6 MONTH)), '-', YEAR(DATE_SUB(NOW(), INTERVAL 6 MONTH))+1, '学年第二学期');
    
    -- 备份到历史表（需要先创建历史表）
    -- INSERT INTO course_history SELECT * FROM course WHERE semester = old_semester;
    -- INSERT INTO enrollment_history SELECT * FROM enrollment WHERE semester = old_semester;
    -- INSERT INTO teaching_history SELECT * FROM teaching WHERE semester = old_semester;
    
    -- 删除过期数据（实际生产环境建议先备份再删除）
    DELETE FROM enrollment WHERE semester = old_semester;
    DELETE FROM teaching WHERE semester = old_semester;
    DELETE FROM course WHERE semester = old_semester;
    
    -- 记录操作日志
    INSERT INTO system_log (operation, details, operator) 
    VALUES ('clean_old_courses', CONCAT('清理学期：', old_semester), 'system');
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
