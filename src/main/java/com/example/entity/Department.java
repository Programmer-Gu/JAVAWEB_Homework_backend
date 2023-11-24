package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Department {
    @TableId(type = IdType.AUTO)
    private int departmentId;
    private String departmentName;
    private int parentDepartmentId;
}
