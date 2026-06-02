package com.example.coursequerysystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.coursequerysystem.common.Result;
import com.example.coursequerysystem.entity.Classroom;
import com.example.coursequerysystem.vo.ClassroomAvailableQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClassroomService extends IService<Classroom> {

    IPage<Classroom> getClassroomPage(Integer current, Integer size, String keyword);



    /**
     * 查询空闲教室
     * @param vo 查询条件
     * @return 空闲教室列表
     */
    List<Classroom> getAvailableClassrooms(ClassroomAvailableQueryVO vo);

    void addClassroom(Classroom classroom);
}