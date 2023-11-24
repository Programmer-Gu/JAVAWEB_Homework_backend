package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Positions {
    @TableId(type = IdType.AUTO)
    private int positionsId;
    private String positionsName;
    private String description;
    private float salary;
}
