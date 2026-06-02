package com.example.coursequerysystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.coursequerysystem.entity.Teaching;
import com.example.coursequerysystem.vo.TeacherScheduleVO;
import com.example.coursequerysystem.vo.TeachingVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeachingMapper extends BaseMapper<Teaching> {

    /**
     * 查询教师的授课安排（课表形式）
     * @param teacherId 教师工号
     * @param semester  学期（可选）
     */
    List<TeacherScheduleVO> selectTeacherSchedule(@Param("teacherId") String teacherId,
                                                  @Param("semester") String semester);

    /**
     * 检查指定时间范围内是否存在冲突的排课
     * @param teacherId 教师ID（可选）
     * @param roomId    教室ID（可选）
     * @param scheduleId 时间安排ID（必选）
     * @param semester  学期（必选）
     * @param excludeTeachingId 排除的排课ID（修改时使用，新增时传null）
     * @return 冲突的排课记录数
     */
    int countConflictTeaching(@Param("teacherId") String teacherId,
                              @Param("roomId") String roomId,
                              @Param("scheduleId") String scheduleId,
                              @Param("semester") String semester,
                              @Param("excludeTeachingId") String excludeTeachingId);

    /**
     * 分页查询排课记录（支持关键词搜索）
     * @param page    分页对象
     * @param keyword 关键词（教学班ID/课程名/教师名）
     * @param semester 学期（可选）
     * @param teacherId 教师ID（可选）
     * @param roomId 教室ID（可选）
     * @return 分页结果（包含关联名称）
     */
    IPage<TeachingVO> selectTeachingPage(Page<?> page,
                                         @Param("keyword") String keyword,
                                         @Param("semester") String semester,
                                         @Param("teacherId") String teacherId,
                                         @Param("roomId") String roomId);

}