package com.example.entity.role;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Employee implements Serializable {
    private int employeeId;
    private int userId;
    @TableField(fill = FieldFill.INSERT)
    private Date joinDate;
    private int departmentId;
    private int positionsId;
    private int academicTitleId;
}
