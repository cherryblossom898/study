package com.example.coursequerysystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.coursequerysystem.entity.Course;
import com.example.coursequerysystem.vo.CoursePublicVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CourseMapper extends BaseMapper<Course> {
    IPage<CoursePublicVO> selectPublicCoursePage(Page<?> page,
                                                 @Param("courseName") String courseName,
                                                 @Param("semester") String semester,
                                                 @Param("typeId") String typeId);

    /**
     * 查询全部课程（用于公开查询，含类型名称）
     */
    List<CoursePublicVO> selectPublicAllCourses();
}