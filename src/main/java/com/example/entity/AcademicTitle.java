package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class AcademicTitle {
    @TableId(type = IdType.AUTO)
    private int academicTitleId;
    private String titleName;
    private String description;
}
