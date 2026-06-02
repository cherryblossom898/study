package com.example.coursequerysystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.coursequerysystem.entity.Teacher;
import com.example.coursequerysystem.vo.TeacherPublicVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeacherMapper extends BaseMapper<Teacher> {
    IPage<TeacherPublicVO> selectPublicTeacherPage(Page<?> page,
                                                   @Param("teacherName") String teacherName,
                                                   @Param("departmentId") String departmentId,
                                                   @Param("title") String title);

    /**
     * 查询全部教师（用于公开查询，含院系名称）
     */
    List<TeacherPublicVO> selectPublicAllTeachers();
}