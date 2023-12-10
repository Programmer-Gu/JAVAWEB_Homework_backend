package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Attendance {
    @TableId(type = IdType.AUTO)
    private int attendanceId;
    private int employeeId;
    private Date date;
    private int status;
    private String realName;
}
