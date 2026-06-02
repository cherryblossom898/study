package com.example.coursequerysystem.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.coursequerysystem.entity.*;
import com.example.coursequerysystem.mapper.*;
import com.example.coursequerysystem.service.TeachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.coursequerysystem.vo.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeachingServiceImpl extends ServiceImpl<TeachingMapper, Teaching> implements TeachingService {

    @Autowired
    private TeachingMapper teachingMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private ClassroomMapper classroomMapper;

    @Autowired
    private TimeScheduleMapper timeScheduleMapper;

    @Override
    public IPage<TeachingVO> getTeachingPage(Integer current, Integer size, String keyword,
                                             String semester, String teacherId, String roomId) {
        Page<?> page = new Page<>(current, size);
        return teachingMapper.selectTeachingPage(page, keyword, semester, teacherId, roomId);
    }

    /**
     * 查询教师的个人授课安排（课表）
     * <p>
     * 作用：
     * 1. 教师登录后，在「我的授课」页面展示自己的周课表
     * 2. 支持按学期筛选，查看不同学期的授课安排
     * 3. 返回数据包含课程名、教室、时间、已选人数等，方便教师掌握教学任务
     * </p>
     * @param teacherId 教师工号
     * @param semester  学期（可选）
     * @return 教师课表数据列表
     */
    @Override
    public List<TeacherScheduleVO> getTeacherSchedule(String teacherId, String semester) {
        return teachingMapper.selectTeacherSchedule(teacherId, semester);
    }


    /**
     * 【管理员】新增排课记录
     * <p>
     * 功能说明：
     * 1. 管理员在排课管理界面填写表单（选择课程、教师、教室、学期、时间安排等）。
     * 2. 后端接收请求后，依次进行业务校验，确保排课数据合理且无冲突。
     * 3. 校验通过后，生成 teaching_id 并保存到数据库。
     * </p>
     *
     * @param vo 排课请求参数，包含课程ID、教师工号、教室编号、时间安排ID、学期、起止日期
     * @return 新生成的排课记录ID（teaching_id）
     * @throws RuntimeException 当数据校验失败或存在冲突时抛出异常，由全局异常处理器捕获
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createTeaching(TeachingCreateVO vo) {
        // 1. 校验基本信息
        Course course = courseMapper.selectById(vo.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        Teacher teacher = teacherMapper.selectById(vo.getTeacherId());
        if (teacher == null) {
            throw new RuntimeException("教师不存在");
        }
        Classroom classroom = classroomMapper.selectById(vo.getRoomId());
        if (classroom == null) {
            throw new RuntimeException("教室不存在");
        }
        TimeSchedule timeSchedule = timeScheduleMapper.selectById(vo.getScheduleId());
        if (timeSchedule == null) {
            throw new RuntimeException("时间安排不存在");
        }

        // 2. 校验教室容量是否足够（可选）
        if (classroom.getCapacity() < course.getMaxStudents()) {
            throw new RuntimeException("教室容量小于课程最大选课人数，请更换教室");
        }

        // 3. 教师时间冲突检测
        int teacherConflict = teachingMapper.countConflictTeaching(
                vo.getTeacherId(), null, vo.getScheduleId(), vo.getSemester(), null);
        if (teacherConflict > 0) {
            throw new RuntimeException("该教师在此时间已有其他课程，请重新选择时间");
        }

        // 4. 教室时间冲突检测
        int roomConflict = teachingMapper.countConflictTeaching(
                null, vo.getRoomId(), vo.getScheduleId(), vo.getSemester(), null);
        if (roomConflict > 0) {
            throw new RuntimeException("该教室在此时间已被占用，请重新选择教室");
        }

        // 5. 保存排课记录
        Teaching teaching = new Teaching();
        teaching.setTeachingId(generateTeachingId());  // 自行生成ID
        teaching.setCourseId(vo.getCourseId());
        teaching.setTeacherId(vo.getTeacherId());
        teaching.setRoomId(vo.getRoomId());
        teaching.setScheduleId(vo.getScheduleId());
        teaching.setSemester(vo.getSemester());
        teaching.setStartDate(vo.getStartDate());
        teaching.setEndDate(vo.getEndDate());
        this.save(teaching);

        return teaching.getTeachingId();
    }

    // 生成 teaching_id 的方法（简单示例）
    private String generateTeachingId() {
        return "TE" + System.currentTimeMillis();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTeaching(TeachingUpdateVO vo) {
        // 1. 检查排课记录是否存在
        Teaching existing = this.getById(vo.getTeachingId());
        if (existing == null) {
            throw new RuntimeException("排课记录不存在");
        }

        // 2. 校验修改后的基本信息（如果前端传了对应字段）
        Course course = null;
        if (vo.getCourseId() != null) {
            course = courseMapper.selectById(vo.getCourseId());
            if (course == null) {
                throw new RuntimeException("课程不存在");
            }
        }

        Teacher teacher = null;
        if (vo.getTeacherId() != null) {
            teacher = teacherMapper.selectById(vo.getTeacherId());
            if (teacher == null) {
                throw new RuntimeException("教师不存在");
            }
        }

        Classroom classroom = null;
        if (vo.getRoomId() != null) {
            classroom = classroomMapper.selectById(vo.getRoomId());
            if (classroom == null) {
                throw new RuntimeException("教室不存在");
            }
        }

        TimeSchedule timeSchedule = null;
        if (vo.getScheduleId() != null) {
            timeSchedule = timeScheduleMapper.selectById(vo.getScheduleId());
            if (timeSchedule == null) {
                throw new RuntimeException("时间安排不存在");
            }
        }

        // 3. 冲突检测（仅当修改了教师、教室或时间安排时才需要检测）
        boolean needCheckConflict = vo.getTeacherId() != null || vo.getRoomId() != null || vo.getScheduleId() != null;
        if (needCheckConflict) {
            String checkTeacherId = vo.getTeacherId() != null ? vo.getTeacherId() : existing.getTeacherId();
            String checkRoomId = vo.getRoomId() != null ? vo.getRoomId() : existing.getRoomId();
            String checkScheduleId = vo.getScheduleId() != null ? vo.getScheduleId() : existing.getScheduleId();
            String checkSemester = vo.getSemester() != null ? vo.getSemester() : existing.getSemester();

            // 教师冲突检测（排除自身）
            int teacherConflict = teachingMapper.countConflictTeaching(
                    checkTeacherId, null, checkScheduleId, checkSemester, vo.getTeachingId());
            if (teacherConflict > 0) {
                throw new RuntimeException("该教师在此时间已有其他课程，请重新选择时间");
            }

            // 教室冲突检测（排除自身）
            int roomConflict = teachingMapper.countConflictTeaching(
                    null, checkRoomId, checkScheduleId, checkSemester, vo.getTeachingId());
            if (roomConflict > 0) {
                throw new RuntimeException("该教室在此时间已被占用，请重新选择教室");
            }
        }

        // 4. 更新字段（只更新前端传递的非 null 字段）
        if (vo.getCourseId() != null) {
            existing.setCourseId(vo.getCourseId());
        }
        if (vo.getTeacherId() != null) {
            existing.setTeacherId(vo.getTeacherId());
        }
        if (vo.getRoomId() != null) {
            existing.setRoomId(vo.getRoomId());
        }
        if (vo.getScheduleId() != null) {
            existing.setScheduleId(vo.getScheduleId());
        }
        if (vo.getSemester() != null) {
            existing.setSemester(vo.getSemester());
        }
        if (vo.getStartDate() != null) {
            existing.setStartDate(vo.getStartDate());
        }
        if (vo.getEndDate() != null) {
            existing.setEndDate(vo.getEndDate());
        }

        this.updateById(existing);
    }

}