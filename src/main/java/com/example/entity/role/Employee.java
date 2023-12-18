package com.example.entity.role;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Employee implements Serializable {
    @TableId(type = IdType.AUTO)
    private int employeeId;
    private int userId;
    @TableField(fill = FieldFill.INSERT)
    private Date joinDate;
    private int departmentId = 0;
    private int positionsId = 0;
    private int academicTitleId = 0;
    private String realName;
    private int retired = 0;
    private double salary = 3000;
}
