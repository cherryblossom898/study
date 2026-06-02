package com.example.coursequerysystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("classroom")
public class Classroom {
    @TableId("room_id")
    private String roomId;

    @TableField("room_name")
    private String roomName;

    @TableField("capacity")
    private Integer capacity;
}